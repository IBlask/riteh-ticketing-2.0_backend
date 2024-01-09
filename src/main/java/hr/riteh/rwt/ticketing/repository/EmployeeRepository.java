package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Employee findByDepartmentIDAndRoleAndActive(int departmentID, char role, boolean active);
}
