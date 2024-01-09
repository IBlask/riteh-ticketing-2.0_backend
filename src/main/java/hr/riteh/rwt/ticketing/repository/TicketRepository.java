package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findById(long ticketID);

    @Query(value = "SELECT agent_user_id FROM Agent_Ticket WHERE ticket_id = :ticketID", nativeQuery = true)
    List<String> findAllAgentsAssignedToTicket(@Param("ticketID") long ticketID);

    @Query(value = "SELECT * FROM Ticket WHERE prijavitelj_user_id = :userID AND status != 'Zaključen'", nativeQuery = true)
    List<Ticket> findAllActiveTicketsByApplicantID(String userID);

    @Query(value = "SELECT * FROM Ticket WHERE prijavitelj_user_id = :userID AND status = 'Zaključen'", nativeQuery = true)
    List<Ticket> findAllClosedTicketsByApplicantID(String userID);

}
