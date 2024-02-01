package hr.riteh.rwt.ticketing.service;

import hr.riteh.rwt.ticketing.auth.JwtUtil;
import hr.riteh.rwt.ticketing.dto.GetAllAgentsResponseDto;
import hr.riteh.rwt.ticketing.dto.SuccessDto;
import hr.riteh.rwt.ticketing.entity.Employee;
import hr.riteh.rwt.ticketing.entity.User;
import hr.riteh.rwt.ticketing.repository.EmployeeRepository;
import hr.riteh.rwt.ticketing.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;

    public ResponseEntity<Object> getAllAgents (HttpServletRequest httpServletRequest) {
        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();
        SuccessDto successDto = new SuccessDto();

        Optional<Employee> employee = employeeRepository.findById(userID);
        if (employee.isEmpty() || employee.get().getRole() != 'v' || !employee.get().isActive()) {
            successDto.setSuccessFalse("Nemate ovlasti dohvatiti popis agenata!");
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(successDto);
        }

        List<Employee> agents = employeeRepository.findAllByDepartmentIDAndRoleAndActive(employee.get().getDepartmentID(), 'a', true);
        List<GetAllAgentsResponseDto> returnList = new ArrayList<>();
        for (Employee agent : agents) {
            User agentDetails = userRepository.findByUserID(agent.getUserID());
            String displayDetails = agentDetails.getFirstName() + " " + agentDetails.getLastName() + " (" + agentDetails.getUserID() + ")";
            GetAllAgentsResponseDto responseDto = new GetAllAgentsResponseDto(displayDetails, agentDetails.getUserID());
            returnList.add(responseDto);
        }

        return ResponseEntity.ok(returnList);
    }
}
