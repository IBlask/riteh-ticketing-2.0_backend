package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
