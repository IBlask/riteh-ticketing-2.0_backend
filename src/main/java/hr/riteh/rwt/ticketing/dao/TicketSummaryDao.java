package hr.riteh.rwt.ticketing.dao;

import java.time.LocalDateTime;

public interface TicketSummaryDao {
    long getId();
    String getTitle();
    String getProstorija();
    int getKategorija_id();
    LocalDateTime getCreated_at();
    String getStatus();
    int getSluzba_id();
    Integer getPriority();
}
