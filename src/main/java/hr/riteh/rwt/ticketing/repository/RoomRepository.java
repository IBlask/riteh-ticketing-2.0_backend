package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    Room findByLabel(String label);

    List<Room> findAllByInstitutionID(int institutionID);
}
