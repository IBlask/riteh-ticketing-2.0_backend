package hr.riteh.rwt.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;
import java.sql.Time;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "Ticket")
public class Ticket {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "parent_id")
    private long parentID;
    @Column(name = "description")
    private String description;
    @Column(name = "sluzba_id")
    private int sluzbaID;
    @Column(name = "voditelj_user_id")
    private String voditeljID;
    @Column(name = "prostorija")
    private String prostorija;
    @Column(name = "institucija_id")
    private int institucijaID;
    @Column(name = "prijavitelj_user_id")
    private String prijaviteljID;
    @Column(name = "stvarni_prijavitelj_user_id")
    private String stvarniPrijaviteljID;
    @Column(name = "kategorija_id")
    private String kategorijaID;
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
