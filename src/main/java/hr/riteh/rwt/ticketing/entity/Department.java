package hr.riteh.rwt.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Sluzba")
public class Department {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "institucija_id")
    private int institutionID;


    public int getId() {
        return id;
    }

    public int getInstitutionID() {
        return institutionID;
    }
}
