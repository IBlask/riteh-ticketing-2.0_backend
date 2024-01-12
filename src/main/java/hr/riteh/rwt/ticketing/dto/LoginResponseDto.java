package hr.riteh.rwt.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDto {
    public boolean success = false;
    public String description = "Dogodila se greška!";
    public String role = null;


    public void setSuccessTrue(String role) {
        this.success = true;
        this.description = null;
        this.role = role;
    }

    public void setSuccessFalse(String description) {
        this.success = false;
        this.role = null;
        this.description = description.isBlank() ? "Dogodila se greška!" : description;
    }
}
