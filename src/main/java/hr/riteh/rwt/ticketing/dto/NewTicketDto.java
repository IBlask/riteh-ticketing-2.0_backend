package hr.riteh.rwt.ticketing.dto;

public class NewTicketDto {
    private Long parentID;
    private String description;
    private String room;
    private String realApplicantID;
    private Integer categoryID;


    public Long getParentID() {
        return parentID;
    }

    public String getDescription() {
        return description;
    }

    public String getRoom() {
        return room;
    }

    public String getRealApplicantID() {
        return realApplicantID;
    }

    public Integer getCategoryID() {
        return categoryID;
    }
}
