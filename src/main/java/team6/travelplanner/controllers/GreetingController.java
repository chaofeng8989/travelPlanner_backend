package team6.travelplanner.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import team6.travelplanner.models.Greeting;

import java.util.Optional;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";

    @GetMapping("/{name}")
    public Greeting greeting(@PathVariable String name) {
        return new Greeting(String.format(template, name));
    }
    @GetMapping("/")
    public Greeting greeting(@PathVariable Optional<String> name) {
        return new Greeting(String.format(template, "world"));
    }
}
