package hr.riteh.rwt.ticketing.dto;

public class ChangeStatusDto {
    private Long ticketID;
    private String status;


    public Long getTicketID() {
        return ticketID;
    }

    public String getStatus() {
        return status;
    }
}
