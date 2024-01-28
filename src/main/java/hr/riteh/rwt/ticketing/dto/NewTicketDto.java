package hr.riteh.rwt.ticketing.dto;

public class NewTicketDto {
    private Long parentID;
    private String title;
    private String description;
    private String room;
    private String realApplicantID;
    private Integer categoryID;
    private Integer priority;


    public Long getParentID() {
        return parentID;
    }

    public String getTitle() {
        return title;
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

    public Integer getPriority() {
        return priority;
    }

    public void setRealApplicantIDAsNull() {
        this.realApplicantID = null;
    }

}
