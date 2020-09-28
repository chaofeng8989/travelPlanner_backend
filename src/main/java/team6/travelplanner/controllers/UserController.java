package team6.travelplanner.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import team6.travelplanner.models.User;
import team6.travelplanner.models.UserRepository;

@RestController
@Slf4j
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        //Check if the same username is already registered
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("UserName occupied");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("user created successfully " + user.getUsername());
        return ResponseEntity.ok("User successfully created");
    }
    @GetMapping("/loginfromgithub")
    public ResponseEntity registerFromGithub(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oAuth2User) {
        if (userRepository.findByUsername(oAuth2User.getName()) == null) {
            User user = new User();
            user.setUsername(oAuth2User.getName());
            userRepository.save(user);
        }
        return ResponseEntity.ok(oAuth2User.getName() + "Logged in from Github");
    }
}
