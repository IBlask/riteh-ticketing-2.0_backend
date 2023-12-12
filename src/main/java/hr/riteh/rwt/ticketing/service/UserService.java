package hr.riteh.rwt.ticketing.service;

import hr.riteh.rwt.ticketing.auth.JwtUtil;
import hr.riteh.rwt.ticketing.dto.LoginDto;
import hr.riteh.rwt.ticketing.dto.SuccessDto;
import hr.riteh.rwt.ticketing.entity.User;
import hr.riteh.rwt.ticketing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

}