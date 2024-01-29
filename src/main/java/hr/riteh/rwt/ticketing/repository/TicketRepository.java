package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.dao.TicketIdDao;
import hr.riteh.rwt.ticketing.dao.TicketSummaryDao;
import hr.riteh.rwt.ticketing.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findById(long ticketID);

    @Query(value = "SELECT agent_user_id FROM Agent_Ticket WHERE ticket_id = :ticketID", nativeQuery = true)
    List<String> findAllAgentsAssignedToTicket(@Param("ticketID") long ticketID);

    Optional<TicketIdDao> findTopByOrderByIdDesc();




    @Query(value = "SELECT * FROM Ticket " +
            "WHERE prijavitelj_user_id = :userID " +
            "AND (status = :status OR :status IS NULL) " +
            "AND (priority = :priority OR :priority IS NULL) " +
            "AND (sluzba_id = :departmentID OR :departmentID IS NULL) " +
            "ORDER BY created_at DESC OFFSET :offset ROWS FETCH NEXT 50 ROWS ONLY", nativeQuery = true)
    List<TicketSummaryDao> findAllUsersTickets(String userID, int offset, String status, Integer priority, Integer departmentID);

    @Query(value = "SELECT COUNT(id) FROM Ticket " +
            "WHERE prijavitelj_user_id = :userID " +
            "AND (status = :status OR :status IS NULL) " +
            "AND (priority = :priority OR :priority IS NULL) " +
            "AND (sluzba_id = :departmentID OR :departmentID IS NULL)", nativeQuery = true)
    long countUsersTickets(String userID, String status, Integer priority, Integer departmentID);



    @Query(value = "SELECT * FROM Ticket " +
            "WHERE (prijavitelj_user_id = :userID OR id IN (SELECT ticket_id FROM Agent_Ticket WHERE agent_user_id = :userID)) " +
            "AND (status = :status OR :status IS NULL) " +
            "AND (priority = :priority OR :priority IS NULL) " +
            "AND (sluzba_id = :departmentID OR :departmentID IS NULL) " +
            "ORDER BY created_at DESC OFFSET :offset ROWS FETCH NEXT 50 ROWS ONLY", nativeQuery = true)
    List<TicketSummaryDao> findAllAgentsTickets(String userID, int offset, String status, Integer priority, Integer departmentID);

    @Query(value = "SELECT COUNT(id) FROM Ticket " +
            "WHERE (prijavitelj_user_id = :userID OR id IN (SELECT ticket_id FROM Agent_Ticket WHERE agent_user_id = :userID)) " +
            "AND (status = :status OR :status IS NULL) " +
            "AND (priority = :priority OR :priority IS NULL) " +
            "AND (sluzba_id = :departmentID OR :departmentID IS NULL)", nativeQuery = true)
    long countAgentsTickets(String userID, String status, Integer priority, Integer departmentID);



    @Query(value = "SELECT * FROM Ticket " +
            "WHERE (prijavitelj_user_id = :userID OR voditelj_user_id = :userID) " +
            "AND (status = :status OR :status IS NULL) " +
            "AND (priority = :priority OR :priority IS NULL) " +
            "AND (sluzba_id = :departmentID OR :departmentID IS NULL) " +
            "ORDER BY created_at DESC OFFSET :offset ROWS FETCH NEXT 50 ROWS ONLY", nativeQuery = true)
    List<TicketSummaryDao> findAllDepartmentLeadersTickets(String userID, int offset, String status, Integer priority, Integer departmentID);

    @Query(value = "SELECT COUNT(id) FROM Ticket " +
            "WHERE (prijavitelj_user_id = :userID OR voditelj_user_id = :userID) " +
            "AND (status = :status OR :status IS NULL) " +
            "AND (priority = :priority OR :priority IS NULL) " +
            "AND (sluzba_id = :departmentID OR :departmentID IS NULL)", nativeQuery = true)
    long countDepartmentLeadersTickets(String userID, String status, Integer priority, Integer departmentID);



    @Query(value = "SELECT * FROM Ticket " +
            "WHERE (prijavitelj_user_id = :userID OR institucija_id = " +
                "(SELECT institucija_id FROM Super_voditelj WHERE user_id = :userID AND active = true)) " +
            "AND (status = :status OR :status IS NULL) " +
            "AND (priority = :priority OR :priority IS NULL) " +
            "AND (sluzba_id = :departmentID OR :departmentID IS NULL) " +
            "ORDER BY created_at DESC OFFSET :offset ROWS FETCH NEXT 50 ROWS ONLY", nativeQuery = true)
    List<TicketSummaryDao> findAllInstitutionLeadersTickets(String userID, int offset, String status, Integer priority, Integer departmentID);

    @Query(value = "SELECT COUNT(id) FROM Ticket " +
            "WHERE (prijavitelj_user_id = :userID OR institucija_id = " +
                "(SELECT institucija_id FROM Super_voditelj WHERE user_id = :userID AND active = true)) " +
            "AND (status = :status OR :status IS NULL) " +
            "AND (priority = :priority OR :priority IS NULL) " +
            "AND (sluzba_id = :departmentID OR :departmentID IS NULL)", nativeQuery = true)
    long countInstitutionLeadersTickets(String userID, String status, Integer priority, Integer departmentID);

}
