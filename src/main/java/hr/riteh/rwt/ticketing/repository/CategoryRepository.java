package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAllByDepartmentIDAndActive(int departmentID, boolean active);
}
