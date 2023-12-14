package hr.riteh.rwt.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "Ticket")
public class Ticket {
    @Id
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
    private String categoryID;
    @Column(name = "created_at")
    private Date createdAt;     // TODO DateTimeFormatter
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
}
