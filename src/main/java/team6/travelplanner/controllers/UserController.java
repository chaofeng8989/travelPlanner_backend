/*
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
import team6.travelplanner.repositories.UserRepository;

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
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("user created successfully " + user.getUsername());
        return ResponseEntity.ok("User successfully created");
    }
    @GetMapping("/loginfromoauth")
    public ResponseEntity registerFromGithub(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oAuth2User) {
        String username = null;
        if (authorizedClient.getClientRegistration().getClientName() == "GitHub") {
            username = oAuth2User.getAttributes().get("login").toString();
        } else if (authorizedClient.getClientRegistration().getClientName() == "Facebook") {
            username = oAuth2User.getAttributes().get("name").toString();
        }
        log.info(username);
        if (userRepository.findByUsername(username) == null) {
            User user = new User();
            user.setUsername(username);
            userRepository.save(user);
        }
        //log.info(oAuth2User.getAttributes().toString());
        //log.info(authorizedClient.getClientRegistration().getClientName());
        return ResponseEntity.ok(username + " Logged in from Third Party");
    }
}
*/
