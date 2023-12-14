package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
