package hr.riteh.rwt.ticketing.controller;

import hr.riteh.rwt.ticketing.dto.GetAllTicketsRequestDto;
import hr.riteh.rwt.ticketing.dto.GetTicketRequestDto;
import hr.riteh.rwt.ticketing.dto.NewTicketDto;
import hr.riteh.rwt.ticketing.dto.SuccessDto;
import hr.riteh.rwt.ticketing.service.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessDto> newTicket (HttpServletRequest httpServletRequest, @RequestBody NewTicketDto newTicketDto) {
        return ticketService.newTicket(httpServletRequest, newTicketDto);
    }

    @PostMapping(value = "/get-all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllTickets (HttpServletRequest httpServletRequest, @RequestBody GetAllTicketsRequestDto requestDto) {
        return ticketService.getAllTickets(httpServletRequest, requestDto);
    }

    @PostMapping(value = "/get-ticket", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTicket (@RequestBody GetTicketRequestDto requestDto) {
        return ticketService.getTicket(requestDto);
    }

}
