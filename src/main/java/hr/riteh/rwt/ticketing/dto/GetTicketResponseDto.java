package hr.riteh.rwt.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GetTicketResponseDto {
    private long ticketID;
    private String description;
    private String room;
    private String category;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String status;
    private String applicantID;
    private String applicantFullName;
    private String realApplicantID;
    private String realApplicantFullName;
    private List<String> agents;
    private String department;
    private String departmentLeaderID;
    private String departmentLeaderFullName;


    public void setTicketID(long ticketID) {
        this.ticketID = ticketID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public void setApplicantFullName(String applicantFullName) {
        this.applicantFullName = applicantFullName;
    }

    public void setRealApplicantID(String realApplicantID) {
        this.realApplicantID = realApplicantID;
    }

    public void setRealApplicantFullName(String realApplicantFullName) {
        this.realApplicantFullName = realApplicantFullName;
    }

    public void setAgents(List<String> agents) {
        this.agents = agents;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDepartmentLeaderID(String departmentLeaderID) {
        this.departmentLeaderID = departmentLeaderID;
    }

    public void setDepartmentLeaderFullName(String departmentLeaderFullName) {
        this.departmentLeaderFullName = departmentLeaderFullName;
    }
}
