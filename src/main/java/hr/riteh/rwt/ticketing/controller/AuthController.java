package hr.riteh.rwt.ticketing.controller;

import hr.riteh.rwt.ticketing.auth.JwtUtil;
import hr.riteh.rwt.ticketing.dto.LoginDto;
import hr.riteh.rwt.ticketing.dto.SuccessDto;
import hr.riteh.rwt.ticketing.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessDto> login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping(value = "/test", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test(@RequestHeader(name = "Authorization") String token) {
        return "Hello world!!";
    }
}
