package hr.riteh.rwt.ticketing.dto;

public class GetAllTicketsRequestDto {
    private String status = null;
    private Integer priority = null;
    private Integer departmentID = null;


    public String getStatus() {
        return status;
    }

    public Integer getPriority() {
        return priority;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }
}
