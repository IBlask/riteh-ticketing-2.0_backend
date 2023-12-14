package hr.riteh.rwt.ticketing.service;

import hr.riteh.rwt.ticketing.auth.JwtUtil;
import hr.riteh.rwt.ticketing.dto.LoginDto;
import hr.riteh.rwt.ticketing.dto.SuccessDto;
import hr.riteh.rwt.ticketing.dto.UserDto;
import hr.riteh.rwt.ticketing.entity.User;
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

@Service
public class UserService {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<SuccessDto> login(LoginDto loginDto) {
        SuccessDto successDto = new SuccessDto();

        //provjera ispravnosti zahtjeva
        if (loginDto.isIncorrectlyFormatted()) {
            successDto.setSuccessFalse("Neispravno formatiran zahtjev!");
            return ResponseEntity.badRequest().body(successDto);
        }

        //provjera prikupljenih podataka
        if (loginDto.getUserID().isBlank() && loginDto.getPassword().isEmpty()) {
            successDto.setSuccessFalse("Unesite korisničko ime i lozinku!");
            return ResponseEntity.badRequest().body(successDto);
        }
        else if (loginDto.getUserID().isBlank()) {
            successDto.setSuccessFalse("Unesite korisničko ime!");
            return ResponseEntity.badRequest().body(successDto);
        }
        else if (loginDto.getPassword().isEmpty()) {
            successDto.setSuccessFalse("Unesite lozinku!");
            return ResponseEntity.badRequest().body(successDto);
        }

        //provjera tocnosti podataka
        User authUser = userRepository.findByUserID(loginDto.getUserID());

        if ((authUser != null) && new BCryptPasswordEncoder().matches(loginDto.getPassword(), authUser.getPassword())) {
            successDto.setSuccessTrue();
            return ResponseEntity.ok()
                    .header("Access-Control-Expose-Headers","Authorization")
                    .header(HttpHeaders.AUTHORIZATION, jwtUtil.createToken(authUser))
                    .body(successDto);
        }

        successDto.setSuccessFalse("Neispravno korisničko ime ili lozinka!");
        return ResponseEntity.ok().body(successDto);
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