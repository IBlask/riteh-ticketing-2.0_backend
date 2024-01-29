package hr.riteh.rwt.ticketing.service;

import hr.riteh.rwt.ticketing.auth.JwtUtil;
import hr.riteh.rwt.ticketing.dao.TicketIdDao;
import hr.riteh.rwt.ticketing.dao.TicketSummaryDao;
import hr.riteh.rwt.ticketing.dto.*;
import hr.riteh.rwt.ticketing.entity.*;
import hr.riteh.rwt.ticketing.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Value("${ticket-photo.path}")
    private String ticketsPhotosPath;


    private int institutionID;
    private Department department;

    public ResponseEntity<SuccessDto> newTicket(HttpServletRequest httpServletRequest, NewTicketDto newTicketDto, MultipartFile ticketImage) {
        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();
        this.institutionID = userRepository.findByUserID(userID).getInstitutionId();

        //verification of data in the DTO
        SuccessDto successDto = verifyNewTicketDto(newTicketDto);
        if (!successDto.isSuccess()) {
            return ResponseEntity.ok(successDto);
        }

        //verifying if user is obligated to set priority
        if (newTicketDto.getPriority() != null) {
            Optional<Employee> employee = employeeRepository.findById(userID);
            if (employee.isEmpty() || employee.get().getRole() != 'v' || employee.get().getDepartmentID() != department.getId()) {
                successDto.setSuccessFalse("Nemate ovlasti mijenjati prioritet ticketa!");
                return ResponseEntity.ok(successDto);
            }
        }

        //SAVING IMAGE
        if (!ticketImage.isEmpty()) {
            try {
                Optional<TicketIdDao> lastTicketID = ticketRepository.findTopByOrderByIdDesc();
                long newTicketID = lastTicketID.map(ticket -> ticket.getId() + 1).orElse(1L);

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
        newTicket.setApplicantID(userID);
        newTicket.setCreatedAt(LocalDateTime.now());
        if (newTicketDto.getParentID() == null) {
            newTicket.setStatus("Otvoren");
            newTicket.setPriority(0);
        }
        else {
            Ticket parent = ticketRepository.findById(newTicket.getParentID().longValue());
            newTicket.setStatus(parent.getStatus());
            newTicket.setPriority(parent.getPriority());
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

        GetTicketResponseDto responseDto = new GetTicketResponseDto();
        responseDto.setTicketID(ticket.getId());
        responseDto.setTitle(ticket.getTitle());
        responseDto.setDescription(ticket.getDescription());
        responseDto.setRoom(ticket.getRoom());
        responseDto.setCategory(categoryRepository.findById(ticket.getCategoryID()).getName());
        responseDto.setCreatedAt(ticket.getCreatedAt());
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
}
