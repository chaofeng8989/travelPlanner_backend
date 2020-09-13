package team6.travelplanner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team6.travelplanner.googleClient.MapClient;
import team6.travelplanner.models.Greeting;
import team6.travelplanner.models.GreetingRepository;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";

    @Autowired
    GreetingRepository repository;

    @Autowired
    MapClient mapClient;

    @GetMapping("/")
    public String root() {
        return "try /nearbyPlaces?lat={lat}&lon={lon} to get nearby places";
    }
    @GetMapping("/greeting/{name}")
    public Greeting greeting(@PathVariable String name) {
        Greeting g =  new Greeting(String.format(template, name));
        repository.save(g);
        return g;
    }
    @GetMapping("/greeting")
    public Greeting greetingWithParam(@RequestParam(value = "name", defaultValue = "World") String name) {
        Greeting g = new Greeting(String.format(template, name));
        repository.save(g);
        mapClient.getNearbyPlaces(47.608013,  -122.335167);
        return g;
    }
}
