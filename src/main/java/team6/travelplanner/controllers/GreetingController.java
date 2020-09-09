package team6.travelplanner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team6.travelplanner.models.Greeting;
import team6.travelplanner.models.GreetingRepository;

import java.util.Optional;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";

    @Autowired
    GreetingRepository repository;
    @GetMapping("/{name}")
    public Greeting greeting(@PathVariable String name) {
        Greeting g =  new Greeting(String.format(template, name));
        repository.save(g);
        return g;
    }
    @GetMapping("/")
    public Greeting greeting(@PathVariable Optional<String> name) {
        Greeting g =  new Greeting(String.format(template, "world"));
        repository.save(g);
        return g;
    }
    @GetMapping("/greeting")
    public Greeting greetingWithParam(@RequestParam(value = "name", defaultValue = "World") String name) {
        Greeting g = new Greeting(String.format(template, name));
        repository.save(g);
        return g;
    }
}
