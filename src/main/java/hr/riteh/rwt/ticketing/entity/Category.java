package hr.riteh.rwt.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Kategorija")
public class Category {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "parent_id")
    private int parentID;
    @Column(name = "sluzba_id")
    private int sluzbaID;
    @Column(name = "active")
    private boolean active;

}
