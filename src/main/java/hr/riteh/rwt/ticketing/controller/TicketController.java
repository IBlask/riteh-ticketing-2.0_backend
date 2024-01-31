package hr.riteh.rwt.ticketing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.riteh.rwt.ticketing.dto.*;
import hr.riteh.rwt.ticketing.service.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    TicketService ticketService;
    @Autowired
    ObjectMapper objectMapper;

    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessDto> newTicket (HttpServletRequest httpServletRequest,
                                                 @RequestParam(value = "jsonData") String jsonData,
                                                 @RequestParam(required = false, value = "image") MultipartFile ticketImage) throws JsonProcessingException {

        if (jsonData.isBlank()) {
            SuccessDto successDto = new SuccessDto();
            successDto.setSuccessFalse("Nema podataka u tijelu zahtjeva!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(successDto);
        }
        NewTicketDto newTicketDto = objectMapper.readValue(jsonData, NewTicketDto.class);
        return ticketService.newTicket(httpServletRequest, newTicketDto, ticketImage);
    }

    @PostMapping(value = "/get-ticket", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTicket (@RequestBody GetTicketRequestDto requestDto) {
        return ticketService.getTicket(requestDto);
    }

    @PostMapping(value = {"/get-all", "/get-all/", "/get-all/{pageNumber}"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAll (HttpServletRequest httpServletRequest, @PathVariable(required = false) Integer pageNumber, @RequestBody GetAllTicketsRequestDto requestDto) {
        return ticketService.getAll(httpServletRequest, requestDto, pageNumber == null ? 1 : pageNumber);
    }

    @PostMapping(value = "/change-status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessDto> changeStatus (HttpServletRequest httpServletRequest, @RequestBody ChangeStatusDto requestDto) {
        return ticketService.changeStatus(httpServletRequest, requestDto);
    }

    @DeleteMapping(value = {"/delete", "/delete/", "/delete/{ticketID}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessDto> deleteTicket (HttpServletRequest httpServletRequest, @PathVariable(required = false) Long ticketID) {
        return ticketService.deleteTicket(httpServletRequest, ticketID);
    }

}
