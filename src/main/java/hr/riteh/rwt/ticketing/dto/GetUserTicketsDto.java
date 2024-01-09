package hr.riteh.rwt.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GetUserTicketsDto {
    private final long ticketID;
    private final String title;
    private final String categoryName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;


    public GetUserTicketsDto(long ticketID, String title, String categoryName, LocalDateTime createdAt) {
        this.ticketID = ticketID;
        this.title = title;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
    }
}
