package hr.riteh.rwt.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserDto {
    private final String userID;
    private final String firstname;
    private final String lastname;

    public UserDto(String userID, String firstname, String lastname) {
        this.userID = userID;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
