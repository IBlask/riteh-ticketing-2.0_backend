package hr.riteh.rwt.ticketing.service;

import hr.riteh.rwt.ticketing.auth.JwtUtil;
import hr.riteh.rwt.ticketing.dto.LoginRequestDto;
import hr.riteh.rwt.ticketing.dto.LoginResponseDto;
import hr.riteh.rwt.ticketing.dto.UserDto;
import hr.riteh.rwt.ticketing.entity.Employee;
import hr.riteh.rwt.ticketing.entity.User;
import hr.riteh.rwt.ticketing.repository.EmployeeRepository;
import hr.riteh.rwt.ticketing.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = new LoginResponseDto();

        //provjera ispravnosti zahtjeva
        if (loginRequestDto.isIncorrectlyFormatted()) {
            loginResponseDto.setSuccessFalse("Neispravno formatiran zahtjev!");
            return ResponseEntity.badRequest().body(loginResponseDto);
        }

        //provjera prikupljenih podataka
        if (loginRequestDto.getUserID().isBlank() && loginRequestDto.getPassword().isEmpty()) {
            loginResponseDto.setSuccessFalse("Unesite korisničko ime i lozinku!");
            return ResponseEntity.badRequest().body(loginResponseDto);
        }
        else if (loginRequestDto.getUserID().isBlank()) {
            loginResponseDto.setSuccessFalse("Unesite korisničko ime!");
            return ResponseEntity.badRequest().body(loginResponseDto);
        }
        else if (loginRequestDto.getPassword().isEmpty()) {
            loginResponseDto.setSuccessFalse("Unesite lozinku!");
            return ResponseEntity.badRequest().body(loginResponseDto);
        }

        //provjera tocnosti podataka
        User authUser = userRepository.findByUserID(loginRequestDto.getUserID());

        if ((authUser != null) && new BCryptPasswordEncoder().matches(loginRequestDto.getPassword(), authUser.getPassword())) {
            Optional<Employee> employee = employeeRepository.findById(loginRequestDto.getUserID());
            if (employee.isPresent()) {
                loginResponseDto.setSuccessTrue(employee.get().getRole() == 'v' ? "voditelj" : "agent");
            }
            else if (employeeRepository.institutionLeaderExistsByUserID(loginRequestDto.getUserID()).isPresent()) {
                loginResponseDto.setSuccessTrue("super-voditelj");
            }
            else {
                loginResponseDto.setSuccessTrue("user");
            }

            return ResponseEntity.ok()
                    .header("Access-Control-Expose-Headers","Authorization")
                    .header(HttpHeaders.AUTHORIZATION, jwtUtil.createToken(authUser))
                    .body(loginResponseDto);
        }

        loginResponseDto.setSuccessFalse("Neispravno korisničko ime ili lozinka!");
        return ResponseEntity.ok().body(loginResponseDto);
    }



    @Value("${user-profile-photo.path}")
    private String dirPath;
    @Value("${user-profile-photo.default-photo}")
    private String defaultPhoto;

    public ResponseEntity<byte[]> getProfilePhoto(HttpServletRequest httpServletRequest) throws IOException {
        Claims claims = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest));

        try {
            return ResponseEntity.ok(IOUtils.toByteArray(new File(dirPath + claims.getSubject() + ".jpg").toURI()));
        } catch (FileNotFoundException e) {
            return ResponseEntity.ok(IOUtils.toByteArray(new File(dirPath + defaultPhoto).toURI()));
        }
    }



    public ResponseEntity<List<UserDto>> getUsers(HttpServletRequest httpServletRequest) {
        List<UserDto> returnList = new ArrayList<>();

        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();
        List<User> usersList = userRepository.findAll();

        for (User user : usersList) {
            if (!user.getUserID().equals(userID)) {
                returnList.add(new UserDto(user.getUserID(), user.getFirstName(), user.getLastName()));
            }
        }

        return ResponseEntity.ok(returnList);
    }

}