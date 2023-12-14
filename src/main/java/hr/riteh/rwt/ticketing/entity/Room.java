package hr.riteh.rwt.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Prostorija")
public class Room {
    @Id
    @Column(name = "oznaka")
    private String label;
    @Column(name = "institucija_id")
    private int institucijaID;

}
