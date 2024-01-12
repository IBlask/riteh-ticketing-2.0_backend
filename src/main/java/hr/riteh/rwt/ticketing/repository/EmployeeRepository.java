package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Employee findByDepartmentIDAndRoleAndActive(int departmentID, char role, boolean active);

    @Query(value = "SELECT user_id FROM Super_voditelj WHERE user_id = :userID", nativeQuery = true)
    Optional<String> institutionLeaderExistsByUserID(String userID);
}
