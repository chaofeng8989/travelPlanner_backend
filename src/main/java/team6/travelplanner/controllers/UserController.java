package team6.travelplanner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team6.travelplanner.models.User;
import team6.travelplanner.models.UserRepository;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        userRepository.save(user);
        System.out.println("in " + user.toString());
        return ResponseEntity.accepted().build();
    }
}
