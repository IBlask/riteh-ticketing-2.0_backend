package hr.riteh.rwt.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RecentlyOpenedTicketsDto {
    public long ticketID;
    private String title;
    private String roomName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String applicant;
    private String status;


    public RecentlyOpenedTicketsDto(long ticketID, String title, String roomName, LocalDateTime createdAt, String applicant, String status) {
        this.ticketID = ticketID;
        this.title = title;
        this.roomName = roomName;
        this.createdAt = createdAt;
        this.applicant = applicant;
        this.status = status;
    }
}
