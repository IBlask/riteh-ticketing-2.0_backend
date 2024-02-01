package hr.riteh.rwt.ticketing.controller;

import hr.riteh.rwt.ticketing.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping(value = "/get-all-agents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllAgents (HttpServletRequest httpServletRequest) {
        return employeeService.getAllAgents(httpServletRequest);
    }
}
