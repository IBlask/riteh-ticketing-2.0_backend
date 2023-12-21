package hr.riteh.rwt.ticketing.service;

import hr.riteh.rwt.ticketing.auth.JwtUtil;
import hr.riteh.rwt.ticketing.entity.Room;
import hr.riteh.rwt.ticketing.repository.RoomRepository;
import hr.riteh.rwt.ticketing.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoomRepository roomRepository;

    public ResponseEntity<List<String>> getAll(HttpServletRequest httpServletRequest) {
        String userID = jwtUtil.resolveClaims(jwtUtil.resolveToken(httpServletRequest)).getSubject();
        int institutionID = userRepository.findByUserID(userID).getInstitutionId();

        List<String> returnList = new ArrayList<>();

        List<Room> roomList = roomRepository.findAllByInstitutionID(institutionID);
        for (Room room : roomList) {
            returnList.add(room.getLabel());
        }

        return ResponseEntity.ok(returnList);
    }
}
