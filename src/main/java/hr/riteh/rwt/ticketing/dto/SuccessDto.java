package hr.riteh.rwt.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessDto {
    private boolean success = false;
    private String description = "Dogodila se greška!";


    public void setSuccessTrue() {
        this.success = true;
        this.description = null;
    }

    public void setSuccessFalse(String description) {
        this.success = false;
        if (!description.isBlank()) {
            this.description = description;
        }
    }


    public boolean isSuccess() {
        return success;
    }

    public String getDescription() {
        return description;
    }

}
