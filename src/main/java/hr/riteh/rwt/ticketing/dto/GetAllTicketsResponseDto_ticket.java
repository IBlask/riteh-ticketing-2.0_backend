package hr.riteh.rwt.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GetAllTicketsResponseDto_ticket {
    private final long ticketID;
    private final String description;


    public GetAllTicketsResponseDto_ticket(long ticketID, String description) {
        this.ticketID = ticketID;
        this.description = description;
    }

}
