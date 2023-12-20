package hr.riteh.rwt.ticketing.service;

import hr.riteh.rwt.ticketing.auth.JwtUtil;
import hr.riteh.rwt.ticketing.dto.*;
import hr.riteh.rwt.ticketing.entity.*;
import hr.riteh.rwt.ticketing.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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


    List<String> supportedStatuses = Arrays.asList("otvoren", "rjesavanje", "na_cekanju", "rijesen", "zakljucen");
    List<String> supportedStatusesDisplay = Arrays.asList("Otvoren", "Rješavanje", "Na čekanju", "Riješen", "Zaključen");

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
        newTicket.setDepartmentLeaderID(userRepository.getDepartmentLeaderByDepartmentID(department.getId()));
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
        if (newTicketDto.getDescription() == null || newTicketDto.getDescription().isBlank()) {
            successDto.setSuccessFalse("Molimo unesite opis!");
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
        if (realApplicantID != null) {
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




    public ResponseEntity getAllTickets (HttpServletRequest httpServletRequest, GetAllTicketsRequestDto requestDto) {
        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();
        this.institutionID = userRepository.findByUserID(userID).getInstitutionId();

        //verification of data in the DTO
        SuccessDto successDto = verifyGetAllTicketsRequestDto(requestDto);
        if (!successDto.isSuccess()) {
            return ResponseEntity.ok(successDto);
        }

        //sending response data
        boolean myTickets = requestDto.isMyTickets();
        Integer departmentID = requestDto.getDepartmentID();
        String status = requestDto.getStatus();

        List<Ticket> tickets = null;
        //my tickets
        if (myTickets) {
            if (departmentID != null && status != null) {
                tickets = ticketRepository.findAllMyTicketsByDepartmentIdAndStatus(userID, departmentID, mapStatus(status));
            }
            else if (departmentID != null) {
                tickets = ticketRepository.findAllMyTicketsByDepartmentId(userID, departmentID);
            }
            else if (status != null) {
                tickets = ticketRepository.findAllMyTicketsByStatus(userID, mapStatus(status));
            }
            else {
                tickets = ticketRepository.findAllMyTickets(userID);
            }
        }
        //all tickets
        else {
            Employee employee = null;
            if (employeeRepository.existsById(userID)) {
                employee = employeeRepository.findByUserID(userID);
            }
            if (employee == null) {
                tickets = ticketRepository.findAllMyTickets(userID);
            }
            else if (employee.getRole() == 'v') {
                if (employee.isActive()) {
                    if (status != null) {
                        tickets = ticketRepository.findAllByDepartmentIDAndStatus(employee.getDepartmentID(), mapStatus(status));
                    }
                    else {
                        tickets = ticketRepository.findAllByDepartmentID(employee.getDepartmentID());
                    }
                }
                else {
                    if (status != null) {
                        tickets = ticketRepository.findAllByDepartmentLeaderIDAndStatus(userID, mapStatus(status));
                    }
                    else {
                        tickets = ticketRepository.findAllByDepartmentLeaderID(userID);
                    }
                }
            }
            else if (employee.getRole() == 'a') {
                List<Long> ticketsID = ticketRepository.findAllTicketsIDsAssignedToAgent(userID);
                if (status != null) {
                    tickets = ticketRepository.findAllByIdInAndStatus(ticketsID, mapStatus(status));
                }
                else {
                    tickets = ticketRepository.findAllByIdIn(ticketsID);
                }
            }
        }

        //mapping
        GetAllTicketsResponseDto responseDto = new GetAllTicketsResponseDto();
        if (tickets != null) {
            for (Ticket ticket : tickets) {
                responseDto.addTicket(new GetAllTicketsResponseDto_ticket(ticket.getId(), ticket.getDescription()));
            }
        }


        //departments
        List<Department> departments = departmentRepository.findAllByInstitutionID(this.institutionID);
        for (Department department : departments) {
            responseDto.addDepartment(new GetAllTicketsResponseDto_department(department.getId(), department.getName()));
        }

        return ResponseEntity.ok(responseDto);
    }

    private SuccessDto verifyGetAllTicketsRequestDto(GetAllTicketsRequestDto requestDto) {
        SuccessDto successDto = new SuccessDto();
        successDto.setSuccessTrue();

        //verification of departmentID (if sent)
        Integer departmentID = requestDto.getDepartmentID();
        if (departmentID != null) {
            this.department = departmentRepository.findById(departmentID.intValue());
            if (this.department == null) {
                successDto.setSuccessFalse("Odabrana služba nije u sustavu!");
                return successDto;
            }
            if (this.department.getInstitutionID() != this.institutionID) {
                successDto.setSuccessFalse("Odabrana služba nije povezana s Vašom institucijom!");
                return successDto;
            }
        }

        //verification of status (if sent)
        if (requestDto.getStatus() != null) {
            if (!this.supportedStatuses.contains(requestDto.getStatus())) {
                successDto.setSuccessFalse("Sustav trenutno ne podržava status koji ste odabrali!");
                return successDto;
            }
        }

        return successDto;
    }

    private String mapStatus(String status) {
        return supportedStatusesDisplay.get(supportedStatuses.indexOf(status));
    }

}
