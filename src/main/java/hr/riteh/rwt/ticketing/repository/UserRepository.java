package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUserID(String userID);

    @Query(value = "SELECT user_id FROM zaposlenik_sluzbe WHERE sluzba_id = :departmentID AND role = 'v' AND active = true", nativeQuery = true)
    String getDepartmentLeaderByDepartmentID(@Param("departmentID") int departmentID);

}
