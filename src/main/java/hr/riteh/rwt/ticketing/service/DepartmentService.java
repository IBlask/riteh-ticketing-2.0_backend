package hr.riteh.rwt.ticketing.service;

import hr.riteh.rwt.ticketing.auth.JwtUtil;
import hr.riteh.rwt.ticketing.dto.GetAllDepartmentsResponseDto;
import hr.riteh.rwt.ticketing.entity.Department;
import hr.riteh.rwt.ticketing.repository.DepartmentRepository;
import hr.riteh.rwt.ticketing.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    public ResponseEntity<Object> getAll(HttpServletRequest httpServletRequest) {
        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();

        List<GetAllDepartmentsResponseDto> returnList = new ArrayList<>();

        int institutionID = userRepository.findByUserID(userID).getInstitutionId();
        List<Department> departmentList = departmentRepository.findAllByInstitutionID(institutionID);
        for (Department department : departmentList) {
            returnList.add(new GetAllDepartmentsResponseDto(department.getId(), department.getName()));
        }

        return ResponseEntity.ok(returnList);
    }
}
