package hr.riteh.rwt.ticketing.entity;

import hr.riteh.rwt.ticketing.dto.NewTicketDto;
import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Table(name = "Ticket")
public class Ticket {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "parent_id")
    private Long parentID;
    @Column(name = "description")
    private String description;
    @Column(name = "sluzba_id")
    private int departmentID;
    @Column(name = "voditelj_user_id")
    private String departmentLeaderID;
    @Column(name = "prostorija")
    private String room;
    @Column(name = "institucija_id")
    private int institutionID;
    @Column(name = "prijavitelj_user_id")
    private String applicantID;
    @Column(name = "stvarni_prijavitelj_user_id")
    private String realApplicantID;
    @Column(name = "kategorija_id")
    private int categoryID;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;     // TODO DateTimeFormatter
    @Column(name = "status")
    private String status;
    @Column(name = "priority")
    private Integer priority;
    @Column(name = "deadline")
    private Date deadline;
    @Column(name = "est_fix_time")
    private Time estFicTime;
    @Column(name = "real_fix_time")
    private Time realFixTime;


    public Ticket(NewTicketDto newTicketDto) {
        this.parentID = newTicketDto.getParentID();
        this.description = newTicketDto.getDescription();
        this.room = newTicketDto.getRoom();
        this.categoryID = newTicketDto.getCategoryID();
        this.realApplicantID = newTicketDto.getRealApplicantID();
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public void setDepartmentLeaderID(String departmentLeaderID) {
        this.departmentLeaderID = departmentLeaderID;
    }

    public void setInstitutionID(int institutionID) {
        this.institutionID = institutionID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setEstFicTime(Time estFicTime) {
        this.estFicTime = estFicTime;
    }

    public void setRealFixTime(Time realFixTime) {
        this.realFixTime = realFixTime;
    }
}
