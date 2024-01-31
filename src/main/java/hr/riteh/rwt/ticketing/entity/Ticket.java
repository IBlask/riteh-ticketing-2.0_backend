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
    @Column(name = "title")
    private String title;
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
    private LocalDateTime createdAt;
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "status")
    private String status;
    @Column(name = "priority")
    private int priority;
    @Column(name = "deadline")
    private Date deadline;
    @Column(name = "est_fix_time")
    private Time estFicTime;
    @Column(name = "real_fix_time")
    private Time realFixTime;
    @Column(name = "visible")
    private boolean visible;


    public Ticket() {
        super();
    }

    public Ticket(NewTicketDto newTicketDto) {
        this.parentID = newTicketDto.getParentID();
        this.title = newTicketDto.getTitle();
        this.description = newTicketDto.getDescription();
        this.room = newTicketDto.getRoom();
        this.categoryID = newTicketDto.getCategoryID();
        this.realApplicantID = newTicketDto.getRealApplicantID();
        this.visible = true;
    }


    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getParentID() {
        return parentID;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public String getDepartmentLeaderID() {
        return departmentLeaderID;
    }

    public String getRoom() {
        return room;
    }

    public int getInstitutionID() {
        return institutionID;
    }

    public String getApplicantID() {
        return applicantID;
    }

    public String getRealApplicantID() {
        return realApplicantID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
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

    public void setRealApplicantID(String realApplicantID) {
        this.realApplicantID = realApplicantID;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }



    public void makeChange(String updatedBy) {
        this.parentID = this.id;
        this.id++;
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }
}
