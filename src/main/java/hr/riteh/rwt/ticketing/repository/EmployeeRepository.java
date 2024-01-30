package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Employee findByDepartmentIDAndRoleAndActive(int departmentID, char role, boolean active);

    @Query(value = "SELECT user_id FROM Super_voditelj WHERE user_id = :userID", nativeQuery = true)
    Optional<String> institutionLeaderExistsByUserID(String userID);

    @Query(value = "SELECT institucija_id FROM Super_voditelj WHERE user_id = :userID AND active = true", nativeQuery = true)
    Optional<Integer> getInstitutionIdByInstitutionLeaderUserID(String userID);
}
