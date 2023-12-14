package hr.riteh.rwt.ticketing.controller;

import hr.riteh.rwt.ticketing.dto.UserDto;
import hr.riteh.rwt.ticketing.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/get-profile-photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getProfilePhoto(HttpServletRequest httpServletRequest) {
        try {
            return userService.getProfilePhoto(httpServletRequest);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping(value = "/get-users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getUsers(HttpServletRequest httpServletRequest) {
        return userService.getUsers(httpServletRequest);
    }

}
