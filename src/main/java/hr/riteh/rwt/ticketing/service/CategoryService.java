package hr.riteh.rwt.ticketing.service;

import hr.riteh.rwt.ticketing.auth.JwtUtil;
import hr.riteh.rwt.ticketing.dto.CategoryDto;
import hr.riteh.rwt.ticketing.entity.Category;
import hr.riteh.rwt.ticketing.entity.Department;
import hr.riteh.rwt.ticketing.repository.CategoryRepository;
import hr.riteh.rwt.ticketing.repository.DepartmentRepository;
import hr.riteh.rwt.ticketing.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    public ResponseEntity<List<CategoryDto>> getAll(HttpServletRequest httpServletRequest) {
        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();
        int institutionID = userRepository.findByUserID(userID).getInstitutionId();
        List<Department> departmentList = departmentRepository.findAllByInstitutionID(institutionID);

        List<CategoryDto> returnList = new ArrayList<>();

        for (Department department : departmentList) {
            List<Category> categoryList = categoryRepository.findAllByDepartmentIDAndActive(department.getId(), true);
            for (Category category : categoryList) {
                CategoryDto categoryDto = new CategoryDto(category.getId(), category.getName());
                returnList.add(categoryDto);
            }
        }

        return ResponseEntity.ok(returnList);
    }

}
