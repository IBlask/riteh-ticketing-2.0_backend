package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    List<Department> findAllByInstitutionID(int institutionID);

    Department findById(int id);
}
