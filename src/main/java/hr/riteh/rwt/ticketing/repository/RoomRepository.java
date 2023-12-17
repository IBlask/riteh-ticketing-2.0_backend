package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    Room findByLabel(String label);
}
