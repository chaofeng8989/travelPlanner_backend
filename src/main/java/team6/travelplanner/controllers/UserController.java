package team6.travelplanner.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import team6.travelplanner.models.User;
import team6.travelplanner.repositories.UserRepository;

import java.security.Principal;

import static team6.travelplanner.Constants.AUTH_SERVER;

@RestController
@Slf4j
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        //Check if the same username is already registered
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("UserName occupied");
        }
        WebClient webClient = WebClient.create(AUTH_SERVER);
        System.out.println("in debug");
        String responseSpec = webClient.post().uri("/register")
                .header("username", user.getUsername())
                .header("password", user.getPassword()).retrieve().bodyToMono(String.class).block();

        return ResponseEntity.ok(responseSpec);
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Principal> get(final Principal principal) {
        return ResponseEntity.ok(principal);
    }
}
