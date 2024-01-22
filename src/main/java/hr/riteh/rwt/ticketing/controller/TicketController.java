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

    @PostMapping(value = "/get-ticket", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTicket (@RequestBody GetTicketRequestDto requestDto) {
        return ticketService.getTicket(requestDto);
    }

    @GetMapping(value = {"/get-all", "/get-all/", "/get-all/{pageNumber}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAll (HttpServletRequest httpServletRequest, @PathVariable(required = false) Integer pageNumber, @RequestBody GetAllTicketsRequestDto requestDto) {
        return ticketService.getAll(httpServletRequest, requestDto, pageNumber == null ? 1 : pageNumber);
    }

}
