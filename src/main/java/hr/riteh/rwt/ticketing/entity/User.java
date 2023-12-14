package hr.riteh.rwt.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @Column(name = "user_id")
    private String userID;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "institucija_id")
    private int institutionId;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;


    public String getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
