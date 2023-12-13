package hr.riteh.rwt.ticketing.controller;

import hr.riteh.rwt.ticketing.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/get-profile-photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getProfilePhoto(HttpServletRequest httpServletRequest) {
        try {
            return userService.getProfilePhoto(httpServletRequest);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
