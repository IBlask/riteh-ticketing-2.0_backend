package hr.riteh.rwt.ticketing.dto;

public class GetAllTicketsRequestDto {
    private boolean myTickets;
    private String status;
    private Integer departmentID;


    public boolean isMyTickets() {
        return myTickets;
    }

    public String getStatus() {
        return status;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }
}
