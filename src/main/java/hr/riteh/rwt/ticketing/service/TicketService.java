package hr.riteh.rwt.ticketing.service;

import hr.riteh.rwt.ticketing.auth.JwtUtil;
import hr.riteh.rwt.ticketing.dao.TicketIdDao;
import hr.riteh.rwt.ticketing.dao.TicketSummaryDao;
import hr.riteh.rwt.ticketing.dto.*;
import hr.riteh.rwt.ticketing.entity.*;
import hr.riteh.rwt.ticketing.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TicketService {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    EntityManager entityManager;

    @Value("${ticket-photo.path}")
    private String ticketsPhotosPath;


    private int institutionID;
    private Department department;
    private final String[] priorityNames = {"Nije određeno", "Nije hitno", "Normalno", "Hitno"};
    private final String[] acceptedStatuses = {"Otvoren", "U rješavanju", "Riješen", "Zaključen"};

    public ResponseEntity<SuccessDto> newTicket(HttpServletRequest httpServletRequest, NewTicketDto newTicketDto, MultipartFile ticketImage) {
        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();
        this.institutionID = userRepository.findByUserID(userID).getInstitutionId();

        //verification of data in the DTO
        SuccessDto successDto = verifyNewTicketDto(newTicketDto);
        if (!successDto.isSuccess()) {
            return ResponseEntity.ok(successDto);
        }

        //verifying if user is obligated to make changes to ticket
        Optional<Employee> employee = employeeRepository.findById(userID);
        boolean obligated = false;
        if (newTicketDto.getParentID() != null) {
            if (ticketRepository.findById(newTicketDto.getParentID().longValue()).getApplicantID().equals(userID)) {
                obligated = true;
            }
            else if (employee.isPresent()) {
                if (employee.get().getRole() == 'a' && employee.get().isActive() && ticketRepository.findAllAgentsAssignedToTicket(newTicketDto.getParentID()).contains(userID)) {
                    obligated = true;
                } else if (employee.get().getRole() == 'v' && employee.get().isActive() && ticketRepository.findById(newTicketDto.getParentID().longValue()).getDepartmentID() == employee.get().getDepartmentID()) {
                    obligated = true;
                }
            }
            else {
                Optional<Integer> leadersInstitutionID = employeeRepository.getInstitutionIdByInstitutionLeaderUserID(userID);
                if (leadersInstitutionID.isPresent() && leadersInstitutionID.get() == this.department.getInstitutionID()) {
                    obligated = true;
                }
            }

            if (!obligated) {
                successDto.setSuccessFalse("Nemate ovlasti raditi izmjene!");
                return ResponseEntity.ok(successDto);
            }
        }

        //change can't have an image
        if (newTicketDto.getParentID() != null && ticketImage != null) {
            successDto.setSuccessFalse("Izmjene ticketa ne mogu imati sliku!");
            return ResponseEntity.ok(successDto);
        }

        //verifying if user is obligated to set priority
        if (newTicketDto.getPriority() != null) {
            if (employee.isEmpty() || employee.get().getRole() != 'v' || employee.get().getDepartmentID() != department.getId()) {
                successDto.setSuccessFalse("Nemate ovlasti mijenjati prioritet ticketa!");
                return ResponseEntity.ok(successDto);
            }
        }

        //SAVING IMAGE
        if (ticketImage != null) {
            try {
                Optional<TicketIdDao> lastTicketID = ticketRepository.findTopByOrderByIdDesc();
                long newTicketID = lastTicketID.map(ticket -> ticket.getId() + 1).orElse(1L);

                Files.createDirectories(Paths.get(ticketsPhotosPath));

                byte[] bytes = ticketImage.getBytes();
                Path path = Paths.get(ticketsPhotosPath + newTicketID + ".jpg");
                Files.write(path, bytes);
            } catch (IOException e) {
                successDto.setSuccessFalse("Došlo je do greške prilikom spremanja fotografije. Molimo pokušajte ponovno.");
                return ResponseEntity.ok(successDto);
            }
        }

        //ENTERING DATA TO THE DATABASE
        Ticket newTicket = new Ticket(newTicketDto);
        newTicket.setDepartmentID(this.department.getId());
        newTicket.setDepartmentLeaderID(employeeRepository.findByDepartmentIDAndRoleAndActive(department.getId(), 'v', true).getUserID());
        newTicket.setInstitutionID(institutionID);
        if (newTicketDto.getParentID() == null) {
            newTicket.setApplicantID(userID);
            newTicket.setStatus("Otvoren");
            newTicket.setPriority(0);
            newTicket.setCreatedAt(LocalDateTime.now());
            newTicket.setUpdatedAt(newTicket.getCreatedAt());
            newTicket.setUpdatedBy(null);
        }
        else {
            Ticket parent = ticketRepository.findById(newTicket.getParentID().longValue());
            newTicket.setApplicantID(parent.getApplicantID());
            if (newTicketDto.getRealApplicantID() == null) newTicket.setRealApplicantID(parent.getRealApplicantID());
            newTicket.setStatus(parent.getStatus());
            newTicket.setPriority(parent.getPriority());
            newTicket.setCreatedAt(parent.getCreatedAt());
            newTicket.setUpdatedAt(LocalDateTime.now());
            newTicket.setUpdatedBy(userID);
        }
        if (newTicketDto.getPriority() != null) {
            newTicket.setPriority(newTicketDto.getPriority());
        }

        ticketRepository.save(newTicket);

        successDto.setSuccessTrue();
        return ResponseEntity.ok(successDto);
    }

    private SuccessDto verifyNewTicketDto (NewTicketDto newTicketDto) {
        SuccessDto successDto = new SuccessDto();
        successDto.setSuccessTrue();

        //verification of mandatory data
        if (newTicketDto.getTitle() == null || newTicketDto.getTitle().isBlank()) {
            successDto.setSuccessFalse("Molimo unesite naslov!");
            return successDto;
        }
        else if (newTicketDto.getRoom() == null || newTicketDto.getRoom().isBlank()) {
            successDto.setSuccessFalse("Molimo odaberite prostoriju!");
            return successDto;
        }
        else if (newTicketDto.getCategoryID() == null) {
            successDto.setSuccessFalse("Molimo odaberite kategoriju!");
            return successDto;
        }

        //verification of the selected room
        Room room = roomRepository.findByLabel(newTicketDto.getRoom());
        if (room == null) {
            successDto.setSuccessFalse("Odabrana prostorija nije u sustavu!");
            return successDto;
        }
        else if (room.getInstitutionID() != this.institutionID) {
            successDto.setSuccessFalse("Odabrana prostorija nije povezana s Vašom institucijom.");
            return successDto;
        }

        //verification of the selected category
        Category category = categoryRepository.findById(newTicketDto.getCategoryID().intValue());
        if (category == null) {
            successDto.setSuccessFalse("Odabrana kategorija nije u sustavu!");
            return successDto;
        }
        this.department = departmentRepository.findById(category.getDepartmentID());
        if (!category.isActive() || this.department == null) {
            successDto.setSuccessFalse("Dogodila se greška u sustavu! Molimo odaberite drugu kategoriju koja najbliže opisuje problem.");
            return successDto;
        }
        else if (this.department.getInstitutionID() != this.institutionID) {
            successDto.setSuccessFalse("Odabrana kategorija nije povezana s Vašom institucijom. Molimo odaberite drugu kategoriju koja najbliže opisuje problem.");
            return successDto;
        }

        //verification of the real applicant (if entered)
        String realApplicantID = newTicketDto.getRealApplicantID();
        if (realApplicantID == null || !realApplicantID.isBlank()) {
            User realApplicant = userRepository.findByUserID(realApplicantID);
            if (realApplicant == null) {
                successDto.setSuccessFalse("Uneseni korisnik nije evidentiran u sustavu!");
                return successDto;
            }
            else if (realApplicant.getInstitutionId() != this.institutionID) {
                successDto.setSuccessFalse("Uneseni korisnik nije povezan s Vašom institucijom!");
                return successDto;
            }
        }
        else {
            newTicketDto.setRealApplicantIDAsNull();
        }

        //verification of the parentID
        if (newTicketDto.getParentID() != null && !ticketRepository.existsById(newTicketDto.getParentID())) {
            successDto.setSuccessFalse("Uneseni ticket-roditelj nije u sustavu!");
            return successDto;
        }

        return successDto;
    }





    public ResponseEntity<Object> getTicket (GetTicketRequestDto requestDto) {
        //verify DTO
        Long ticketID = requestDto.getTicketID();
        SuccessDto successDto = new SuccessDto();
        if (ticketID == null) {
            successDto.setSuccessFalse("Molimo unesite identifikacijski broj ticketa!");
            return ResponseEntity.badRequest().body(successDto);
        }
        else if (!ticketRepository.existsById(ticketID)) {
            successDto.setSuccessFalse("Ne postoji ticket s tim identifikacijskim brojem!");
            return ResponseEntity.badRequest().body(successDto);
        }

        Ticket ticket = ticketRepository.findById(ticketID.longValue());
        List<String> agents = ticketRepository.findAllAgentsAssignedToTicket(ticketID);

        //TICKET DETAILS
        GetTicketResponseDto responseDto = new GetTicketResponseDto();
        responseDto.setTicketID(ticket.getId());
        responseDto.setTitle(ticket.getTitle());
        responseDto.setDescription(ticket.getDescription());
        responseDto.setRoom(ticket.getRoom());
        responseDto.setCategory(categoryRepository.findById(ticket.getCategoryID()).getName());
        responseDto.setCreatedAt(ticket.getCreatedAt());
        responseDto.setLastUpdatedAt(ticket.getUpdatedAt());
        responseDto.setStatus(ticket.getStatus());
        responseDto.setApplicantID(ticket.getApplicantID());
        responseDto.setRealApplicantID(ticket.getRealApplicantID());
        responseDto.setAgents(agents);
        responseDto.setDepartment(departmentRepository.findById(ticket.getDepartmentID()).getName());
        responseDto.setDepartmentLeaderID(ticket.getDepartmentLeaderID());

        User applicant = userRepository.findByUserID(ticket.getApplicantID());
        responseDto.setApplicantFullName(applicant.getFirstName() + " " + applicant.getLastName());

        if (ticket.getRealApplicantID() != null) {
            User realApplicant = userRepository.findByUserID(ticket.getRealApplicantID());
            responseDto.setRealApplicantFullName(realApplicant.getFirstName() + " " + realApplicant.getLastName());
        }

        User departmentLeader = userRepository.findByUserID(ticket.getDepartmentLeaderID());
        responseDto.setDepartmentLeaderFullName(departmentLeader.getFirstName() + " " + departmentLeader.getLastName());

        //TICKET CHANGES
        List<GetTicketChangeDto> ticketChanges = new ArrayList<>();
        while (ticket.getParentID() != null) {
            Optional<Ticket> parentTicketOpt = ticketRepository.findById(ticket.getParentID());
            if (parentTicketOpt.isEmpty()) break;
            Ticket parentTicket = parentTicketOpt.get();

            //priority
            if (ticket.getPriority() != parentTicket.getPriority()) {
                GetTicketChangeDto change = new GetTicketChangeDto();
                change.setUser(ticket.getUpdatedBy());
                change.setChangedAt(ticket.getUpdatedAt());
                change.setChangedField("Prioritet");
                change.setChange(priorityNames[parentTicket.getPriority()] + " -> " + priorityNames[ticket.getPriority()]);
                ticketChanges.add(0, change);
            }
            //status
            if (!Objects.equals(ticket.getStatus(), parentTicket.getStatus())) {
                GetTicketChangeDto change = new GetTicketChangeDto();
                change.setUser(ticket.getUpdatedBy());
                change.setChangedAt(ticket.getUpdatedAt());
                change.setChangedField("Status");
                change.setChange(parentTicket.getStatus() + " -> " + ticket.getStatus());
                ticketChanges.add(0, change);
            }
            //category
            if (ticket.getCategoryID() != parentTicket.getCategoryID()) {
                GetTicketChangeDto change = new GetTicketChangeDto();
                change.setUser(ticket.getUpdatedBy());
                change.setChangedAt(ticket.getUpdatedAt());
                change.setChangedField("Kategorija");
                change.setChange(categoryRepository.findById(parentTicket.getCategoryID()).getName() + " -> " + categoryRepository.findById(ticket.getCategoryID()).getName());
                ticketChanges.add(0, change);
            }
            //real applicant
            if (!Objects.equals(ticket.getRealApplicantID(), parentTicket.getRealApplicantID())) {
                GetTicketChangeDto change = new GetTicketChangeDto();
                change.setUser(ticket.getUpdatedBy());
                change.setChangedAt(ticket.getUpdatedAt());
                change.setChangedField("Stvarni prijavitelj");
                User oldRealApplicant = userRepository.findByUserID(parentTicket.getRealApplicantID());
                User newRealApplicant = userRepository.findByUserID(ticket.getRealApplicantID());
                change.setChange(oldRealApplicant.getFirstName() + " " + oldRealApplicant.getLastName() + " (" + oldRealApplicant.getUserID() + ") "
                        + " -> "
                        + newRealApplicant.getFirstName() + " " + newRealApplicant.getLastName() + " (" + newRealApplicant.getUserID() + ")");
                ticketChanges.add(0, change);
            }
            //room
            if (!Objects.equals(ticket.getRoom(), parentTicket.getRoom())) {
                GetTicketChangeDto change = new GetTicketChangeDto();
                change.setUser(ticket.getUpdatedBy());
                change.setChangedAt(ticket.getUpdatedAt());
                change.setChangedField("Prostorija");
                change.setChange(parentTicket.getRoom() + " -> " + ticket.getRoom());
                ticketChanges.add(0, change);
            }
            //description
            if (!Objects.equals(ticket.getDescription(), parentTicket.getDescription())) {
                GetTicketChangeDto change = new GetTicketChangeDto();
                change.setUser(ticket.getUpdatedBy());
                change.setChangedAt(ticket.getUpdatedAt());
                change.setChangedField("Opis");
                change.setChange(parentTicket.getDescription() + " -> " + ticket.getDescription());
                ticketChanges.add(0, change);
            }
            //title
            if (!Objects.equals(ticket.getTitle(), parentTicket.getTitle())) {
                GetTicketChangeDto change = new GetTicketChangeDto();
                change.setUser(ticket.getUpdatedBy());
                change.setChangedAt(ticket.getUpdatedAt());
                change.setChangedField("Naslov");
                change.setChange(parentTicket.getTitle() + " -> " + ticket.getTitle());
                ticketChanges.add(0, change);
            }

            ticket = parentTicket;
        }
        responseDto.setChanges(ticketChanges);

        //TICKET IMAGE
        String encodedImage;
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(ticketsPhotosPath + ticket.getId() + ".jpg"));
            encodedImage = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            encodedImage = null;
        }
        responseDto.setTicketImage(encodedImage);

        return ResponseEntity.ok(responseDto);
    }





    public ResponseEntity<Object> getAll(HttpServletRequest httpServletRequest, GetAllTicketsRequestDto requestDto, int pageNumber) {
        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();

        if (pageNumber <= 0) {
            SuccessDto successDto = new SuccessDto();
            successDto.setSuccessFalse("Broj stranice mora biti veći od 0!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(successDto);
        }

        List<TicketSummaryDao> tickets;
        long numberOfTickets;
        Optional<Employee> employee = employeeRepository.findById(userID);

        //agent
        if (employee.isPresent() && employee.get().getRole() == 'a') {
            tickets = ticketRepository.findAllAgentsTickets(userID, (pageNumber - 1) * 50, requestDto.getStatus(), requestDto.getPriority(), requestDto.getDepartmentID());
            numberOfTickets = ticketRepository.countAgentsTickets(userID, requestDto.getStatus(), requestDto.getPriority(), requestDto.getDepartmentID());
        }

        //department leader
        else if (employee.isPresent() && employee.get().getRole() == 'v') {
            tickets = ticketRepository.findAllDepartmentLeadersTickets(userID, (pageNumber - 1) * 50, requestDto.getStatus(), requestDto.getPriority(), requestDto.getDepartmentID());
            numberOfTickets = ticketRepository.countDepartmentLeadersTickets(userID, requestDto.getStatus(), requestDto.getPriority(), requestDto.getDepartmentID());
        }

        //institution leader
        else if (employeeRepository.institutionLeaderExistsByUserID(userID).isPresent()) {
            tickets = ticketRepository.findAllInstitutionLeadersTickets(userID, (pageNumber - 1) * 50, requestDto.getStatus(), requestDto.getPriority(), requestDto.getDepartmentID());
            numberOfTickets = ticketRepository.countInstitutionLeadersTickets(userID, requestDto.getStatus(), requestDto.getPriority(), requestDto.getDepartmentID());
        }

        //normal user
        else {
            tickets = ticketRepository.findAllUsersTickets(userID, (pageNumber - 1) * 50, requestDto.getStatus(), requestDto.getPriority(), requestDto.getDepartmentID());
            numberOfTickets = ticketRepository.countUsersTickets(userID, requestDto.getStatus(), requestDto.getPriority(), requestDto.getDepartmentID());
        }

        String[] priorityNames =  {"Nije određeno", "Nije hitno", "Normalno", "Hitno"};
        List<GetAllTicketsResponseDto> returnList = new ArrayList<>();
        for (TicketSummaryDao ticket : tickets) {
            String categoryName = categoryRepository.findById(ticket.getKategorija_id()).getName();
            String departmentName = departmentRepository.findById(ticket.getSluzba_id()).getName();
            String priority = priorityNames[ticket.getPriority()];

            returnList.add(new GetAllTicketsResponseDto(ticket, categoryName, departmentName, priority));
        }

        long numberOfPages = numberOfTickets / 50;
        if (numberOfTickets % 50 != 0) numberOfPages++;
        if (numberOfTickets == 0) pageNumber = 0;

        return ResponseEntity.ok()
                .header("Access-Control-Expose-Headers","numberOfPages, currentPage")
                .header("numberOfPages", String.valueOf(numberOfPages))
                .header("currentPage", String.valueOf(pageNumber))
                .body(returnList);
    }





    public ResponseEntity<SuccessDto> changeStatus(HttpServletRequest httpServletRequest, ChangeStatusDto requestDto) {
        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();
        SuccessDto successDto = new SuccessDto();

        //verify DTO
        if (requestDto.getTicketID() == null) {
            successDto.setSuccessFalse("Niste odabrali ticket kojemu želite promijeniti status!");
            return ResponseEntity.badRequest().body(successDto);
        }
        Optional<Ticket> ticket = ticketRepository.findById(requestDto.getTicketID());
        if (ticket.isEmpty()) {
            successDto.setSuccessFalse("Ne postoji ticket s tim identifikacijskim brojem!");
            return ResponseEntity.badRequest().body(successDto);
        }
        else if (ticketRepository.findChild(requestDto.getTicketID()).isPresent()) {
            successDto.setSuccessFalse("Odabranom ticketu nije moguće mijenjati status!");
            return ResponseEntity.badRequest().body(successDto);
        }
        if (requestDto.getStatus() == null || requestDto.getStatus().isBlank()) {
            successDto.setSuccessFalse("Niste odabrali status!");
            return ResponseEntity.badRequest().body(successDto);
        }
        else if (Arrays.stream(acceptedStatuses).noneMatch(requestDto.getStatus()::equals)) {
            successDto.setSuccessFalse("Sustav trenutno ne podržava odabrani status!");
            return ResponseEntity.badRequest().body(successDto);
        }

        //verify if user is obligated to change status
        Optional<Employee> employee = employeeRepository.findById(userID);
        boolean obligated = false;
        if (employee.isPresent() && employee.get().isActive()) {
            if (employee.get().getRole() == 'a' && ticketRepository.findAllAgentsAssignedToTicket(requestDto.getTicketID()).contains(userID) && !requestDto.getStatus().equals(acceptedStatuses[acceptedStatuses.length-1])) {
                obligated = true;
            }
            else if (employee.get().getRole() == 'v') {
                if (ticketRepository.findById(requestDto.getTicketID()).isEmpty()) {
                    successDto.setSuccessFalse("Ne postoji ticket s tim identifikacijskim brojem!");
                    return ResponseEntity.badRequest().body(successDto);
                }
                else if (employee.get().getDepartmentID() == ticketRepository.findById(requestDto.getTicketID().longValue()).getDepartmentID()) {
                    obligated = true;
                }
            }
        }
        if (!obligated) {
            successDto.setSuccessFalse("Nemate ovlasti mijenjati status ticketu!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(successDto);
        }

        //CHANGE STATUS
        Ticket newTicket = ticket.get();
        entityManager.detach(newTicket);
        newTicket.setStatus(requestDto.getStatus());
        newTicket.makeChange(userID);
        ticketRepository.save(newTicket);
        successDto.setSuccessTrue();
        return ResponseEntity.ok(successDto);
    }
}
