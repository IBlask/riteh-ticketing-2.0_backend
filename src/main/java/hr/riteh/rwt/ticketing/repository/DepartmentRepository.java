package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    List<Department> findAllByInstitutionID(int institutionID);
}
