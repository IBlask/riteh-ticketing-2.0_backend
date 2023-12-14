package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitutionRepository extends JpaRepository<Institution, Integer> {
}
