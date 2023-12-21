package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findById(long ticketID);

    //MY TICKETS
    @Query(value = "SELECT * FROM Ticket WHERE prijavitelj_user_id = :userID OR stvarni_prijavitelj_user_id = :userID", nativeQuery = true)
    List<Ticket> findAllMyTickets(@Param("userID") String userID);

    @Query(value = "SELECT * FROM Ticket WHERE (prijavitelj_user_id = :userID OR stvarni_prijavitelj_user_id = :userID) AND sluzba_id = :departmentID", nativeQuery = true)
    List<Ticket> findAllMyTicketsByDepartmentId(@Param("userID") String userID, @Param("departmentID") int departmentID);

    @Query(value = "SELECT * FROM Ticket WHERE (prijavitelj_user_id = :userID OR stvarni_prijavitelj_user_id = :userID) AND status = :status", nativeQuery = true)
    List<Ticket> findAllMyTicketsByStatus(@Param("userID") String userID, @Param("status") String status);

    @Query(value = "SELECT * FROM Ticket WHERE (prijavitelj_user_id = :userID OR stvarni_prijavitelj_user_id = :userID) AND sluzba_id = :departmentID AND status = :status", nativeQuery = true)
    List<Ticket> findAllMyTicketsByDepartmentIdAndStatus(@Param("userID") String userID, @Param("departmentID") int departmentID, @Param("status") String status);




    //ALL TICKETS
    List<Ticket> findAllByInstitutionID(int institutionID);

    List<Ticket> findAllByDepartmentID(int departmentID);

    List<Ticket> findAllByDepartmentLeaderID(String departmentLeaderID);

    @Query(value = "SELECT ticket_id FROM Agent_Ticket WHERE agent_user_id = :userID", nativeQuery = true)
    List<Long> findAllTicketsIDsAssignedToAgent(@Param("userID") String userID);
    List<Ticket> findAllByIdIn(List<Long> ticketsIDs);

    List<Ticket> findAllByInstitutionIDAndStatus(int institutionID, String status);

    List<Ticket> findAllByDepartmentIDAndStatus(int departmentID, String status);

    List<Ticket> findAllByDepartmentLeaderIDAndStatus(String departmentLeaderID, String status);

    List<Ticket> findAllByIdInAndStatus(List<Long> ticketsIDs, String status);



    //FIND AGENTS ASSIGNED TO TICKET
    @Query(value = "SELECT agent_user_id FROM Agent_Ticket WHERE ticket_id = :ticketID", nativeQuery = true)
    List<String> findAllAgentsAssignedToTicket(@Param("ticketID") long ticketID);

}
