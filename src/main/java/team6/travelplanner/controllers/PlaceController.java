package team6.travelplanner.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team6.travelplanner.googleClient.MapClient;
import team6.travelplanner.models.City;
import team6.travelplanner.models.PagedResponse;
import team6.travelplanner.models.Place;
import team6.travelplanner.models.Tour;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class PlaceController {
    @Autowired
    MapClient mapClient;

    @GetMapping("/place/search")
    public PagedResponse getPlaceSearch(@RequestParam(value = "lat", defaultValue = "47.608013") double lat,
                                        @RequestParam(value = "lon", defaultValue = "-122.335167") double lon,
                                        @RequestParam(value = "nextPageToken", defaultValue = "") String token) {
        LocalTime localTime = LocalTime.now();
        PagedResponse res = null;
        if (token.isBlank()) res = mapClient.getNearbyPlaces(lat,  lon);
        else res = mapClient.getNearbyPlacesNextPage(token);

        log.info("query time: " + (LocalTime.now().getSecond() - localTime.getSecond() + 60) % 60);
        return res;
    }

    @GetMapping("/place/{placeId}")
    public Place getPlaceDetails(@PathVariable String placeId) {
        Place place = mapClient.getPlaceDetails(placeId);
        return place;
    }


    @GetMapping("/city")
    public City getCity() {
        City test = new City(Arrays.asList("culture", "modern"), Arrays.asList("walking", "driving", "bicycling", "transit"));
        return test;
    }


    @PostMapping("/city")
    public ResponseEntity recommendTour(@RequestParam String City, @RequestParam String interest, @RequestParam String transportation) {
        return ResponseEntity.ok("TO DO");
    }

    @PostMapping("/place")
    public ResponseEntity getTour(@RequestBody List<Place> places) {
        return ResponseEntity.ok("TO DO");
    }

}
