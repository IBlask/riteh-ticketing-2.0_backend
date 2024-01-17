package hr.riteh.rwt.ticketing.service;

import hr.riteh.rwt.ticketing.auth.JwtUtil;
import hr.riteh.rwt.ticketing.dto.*;
import hr.riteh.rwt.ticketing.entity.*;
import hr.riteh.rwt.ticketing.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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


    private int institutionID;
    private Department department;

    public ResponseEntity<SuccessDto> newTicket(HttpServletRequest httpServletRequest, NewTicketDto newTicketDto) {
        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();
        this.institutionID = userRepository.findByUserID(userID).getInstitutionId();

        //verification of data in the DTO
        SuccessDto successDto = verifyNewTicketDto(newTicketDto);
        if (!successDto.isSuccess()) {
            return ResponseEntity.ok(successDto);
        }

        //ENTERING DATA TO THE DATABASE
        Ticket newTicket = new Ticket(newTicketDto);
        newTicket.setDepartmentID(this.department.getId());
        newTicket.setDepartmentLeaderID(employeeRepository.findByDepartmentIDAndRoleAndActive(department.getId(), 'v', true).getUserID());
        newTicket.setInstitutionID(institutionID);
        newTicket.setApplicantID(userID);
        newTicket.setCreatedAt(LocalDateTime.now());
        newTicket.setStatus("Otvoren");

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
        if (this.department == null) {
            successDto.setSuccessFalse("Dogodila se greška u sustavu! Molimo odaberite drugu kategoriju koja najbliže opisuje problem.");
            return successDto;
        }
        else if (this.department.getInstitutionID() != this.institutionID) {
            successDto.setSuccessFalse("Odabrana kategorija nije povezana s Vašom institucijom. Molimo odaberite drugu kategoriju koja najbliže opisuje problem.");
            return successDto;
        }

        //verification of the real applicant (if entered)
        String realApplicantID = newTicketDto.getRealApplicantID();
        if (!realApplicantID.isBlank()) {
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

        //verification of the parentID
        if (newTicketDto.getParentID() != null && !ticketRepository.existsById(newTicketDto.getParentID())) {
            successDto.setSuccessFalse("Uneseni ticket-roditelj nije u sustavu!");
            return successDto;
        }

        return successDto;
    }





    public ResponseEntity getTicket (GetTicketRequestDto requestDto) {
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





    public ResponseEntity getActiveTickets(HttpServletRequest httpServletRequest) {
        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();

        List<GetUserTicketsDto> returnList = new ArrayList<>();
        List<Ticket> tickets = ticketRepository.findAllActiveTicketsByApplicantID(userID);

        for (Ticket ticket : tickets) {
            String categoryName = categoryRepository.findById(ticket.getCategoryID()).getName();
            GetUserTicketsDto ticketDto = new GetUserTicketsDto(ticket.getId(), ticket.getTitle(), categoryName, ticket.getCreatedAt());
            returnList.add(ticketDto);
        }

        return ResponseEntity.ok(returnList);
    }





    public ResponseEntity getClosedTickets(HttpServletRequest httpServletRequest) {
        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();

        List<GetUserTicketsDto> returnList = new ArrayList<>();
        List<Ticket> tickets = ticketRepository.findAllClosedTicketsByApplicantID(userID);

        for (Ticket ticket : tickets) {
            String categoryName = categoryRepository.findById(ticket.getCategoryID()).getName();
            GetUserTicketsDto ticketDto = new GetUserTicketsDto(ticket.getId(), ticket.getTitle(), categoryName, ticket.getCreatedAt());
            returnList.add(ticketDto);
        }

        return ResponseEntity.ok(returnList);
    }





    public ResponseEntity getRecentlyOpenedTickets(HttpServletRequest httpServletRequest) {
        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();
        Optional<User> user = userRepository.findById(userID);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        int institutionID = user.get().getInstitutionId();

        List<RecentlyOpenedTicketsDto> returnList = new ArrayList<>();
        List<Ticket> tickets = ticketRepository.findAllRecentlyOpenedTickets(institutionID, LocalDateTime.now().minusDays(2));

        for (Ticket ticket : tickets) {
            User applicant = userRepository.findByUserID(ticket.getApplicantID());
            RecentlyOpenedTicketsDto ticketDto = new RecentlyOpenedTicketsDto(
                    ticket.getId(),
                    ticket.getTitle(),
                    ticket.getRoom(),
                    ticket.getCreatedAt(),
                    applicant.getFirstName() + " " + applicant.getLastName() + " " + applicant.getUserID(),
                    ticket.getStatus());
            returnList.add(ticketDto);
        }

        return ResponseEntity.ok(returnList);
    }
}
