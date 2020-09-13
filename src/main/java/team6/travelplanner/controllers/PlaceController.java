package team6.travelplanner.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team6.travelplanner.googleClient.MapClient;
import team6.travelplanner.models.Greeting;
import team6.travelplanner.models.Place;

import java.util.Iterator;
import java.util.Set;

@RestController
public class PlaceController {
    @Autowired
    MapClient mapClient;

    @GetMapping("/nearbyPlaces")
    public Set<Place> greetingWithParam(@RequestParam(value = "lat", defaultValue = "47.608013") double lat,
                                        @RequestParam(value = "lon", defaultValue = "-122.335167") double lon) {
        Set<Place> resultSet = mapClient.getNearbyPlaces(lat,  lon);
        return resultSet;
    }
}
