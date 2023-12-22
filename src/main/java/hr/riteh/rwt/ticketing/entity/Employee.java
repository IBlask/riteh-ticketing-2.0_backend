package hr.riteh.rwt.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Zaposlenik_sluzbe")
public class Employee {
    @Id
    @Column(name = "user_id")
    private String userID;
    @Column(name = "sluzba_id")
    private int departmentID;
    @Column(name = "role")
    private char role;
    @Column(name = "active")
    private boolean active;


    public String getUserID() {
        return userID;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public char getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }
}
