package hr.riteh.rwt.ticketing.repository;

import hr.riteh.rwt.ticketing.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
