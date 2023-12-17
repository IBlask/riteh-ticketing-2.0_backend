package hr.riteh.rwt.ticketing.service;

import hr.riteh.rwt.ticketing.auth.JwtUtil;
import hr.riteh.rwt.ticketing.dto.NewTicketDto;
import hr.riteh.rwt.ticketing.dto.SuccessDto;
import hr.riteh.rwt.ticketing.entity.*;
import hr.riteh.rwt.ticketing.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

        //TODO ----
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

}
