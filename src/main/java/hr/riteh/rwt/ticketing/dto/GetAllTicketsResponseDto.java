package hr.riteh.rwt.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import hr.riteh.rwt.ticketing.dao.TicketSummaryDao;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GetAllTicketsResponseDto {
    private final long ticketID;
    private final String title;
    private final String roomName;
    private final String categoryName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    private final String status;
    private final String departmentName;
    private final String priority;


    public GetAllTicketsResponseDto(TicketSummaryDao dao, String categoryName, String departmentName, String priority) {
        this.ticketID = dao.getId();
        this.title = dao.getTitle();
        this.roomName = dao.getProstorija();
        this.categoryName = categoryName;
        this.createdAt = dao.getCreated_at();
        this.status = dao.getStatus();
        this.departmentName = departmentName;
        this.priority = priority;
    }
}
