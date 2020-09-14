package team6.travelplanner.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team6.travelplanner.googleClient.MapClient;
import team6.travelplanner.models.Greeting;
import team6.travelplanner.models.Place;

import java.time.LocalTime;
import java.util.Iterator;
import java.util.Set;

@RestController
public class PlaceController {
    @Autowired
    MapClient mapClient;

    @GetMapping("/place/search")
    public Set<Place> getPlaceSearch(@RequestParam(value = "lat", defaultValue = "47.608013") double lat,
                                        @RequestParam(value = "lon", defaultValue = "-122.335167") double lon,
                                        @RequestParam(value = "nextPageToken", defaultValue = "") String token) {
        LocalTime localTime = LocalTime.now();
        Set<Place> resultSet = null;
        if (token.isBlank()) resultSet = mapClient.getNearbyPlaces(lat,  lon);
        else resultSet = mapClient.getNearbyPlacesNextPage(token);
        System.out.println("query time: " + (LocalTime.now().getSecond() - localTime.getSecond() + 60) % 60);
        return resultSet;
    }

    @GetMapping("/place/{placeId}")
    public Place getPlaceDetails(@PathVariable String placeId) {
        Place place = mapClient.getPlaceDetails(placeId);
        return place;
    }

}
