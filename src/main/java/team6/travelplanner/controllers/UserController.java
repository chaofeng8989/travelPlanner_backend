package team6.travelplanner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import team6.travelplanner.models.User;
import team6.travelplanner.models.UserRepository;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity register(@RequestParam @Validated User user) {
        userRepository.save(user);
        return ResponseEntity.accepted().build();
    }
}
