package team6.travelplanner.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team6.travelplanner.models.User;
import team6.travelplanner.models.UserRepository;

@RestController
@Slf4j
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        userRepository.save(user);
        log.info("user created successfully " + user.getUsername());
        return ResponseEntity.accepted().build();
    }
}
