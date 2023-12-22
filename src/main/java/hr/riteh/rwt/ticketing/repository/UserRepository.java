package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUserID(String userID);

}
