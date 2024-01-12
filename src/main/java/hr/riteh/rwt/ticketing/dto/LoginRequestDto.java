package hr.riteh.rwt.ticketing.dto;

public class LoginRequestDto {
    private String userID;
    private String password;


    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }


    public boolean isIncorrectlyFormatted() {
        return this.userID == null || this.password == null;
    }
}
