package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}
