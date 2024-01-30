package hr.riteh.rwt.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GetTicketChangeDto {
    private String user;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime changedAt;
    private String changedField;
    private String change;


    public void setUser(String user) {
        this.user = user;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public void setChangedField(String changedField) {
        this.changedField = changedField;
    }

    public void setChange(String change) {
        this.change = change;
    }
}
