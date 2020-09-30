package team6.travelplanner.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team6.travelplanner.googleClient.MapClient;
import team6.travelplanner.models.City;
import team6.travelplanner.models.PagedResponse;
import team6.travelplanner.models.Place;
import team6.travelplanner.models.PlaceRepository;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
public class PlaceController {
    private final MapClient mapClient;
    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceController(MapClient mapClient, PlaceRepository placeRepository) {
        this.mapClient = mapClient;
        this.placeRepository = placeRepository;
    }

    @GetMapping("/place/search")
    public PagedResponse getPlaceSearch(@RequestParam(value = "lat", defaultValue = "47.608013") double lat,
                                        @RequestParam(value = "lon", defaultValue = "-122.335167") double lon,
                                        @RequestParam(value = "nextPageToken", defaultValue = "") String token,
                                        @RequestParam(value = "city", defaultValue = "") String city) {
        LocalTime localTime = LocalTime.now();
        PagedResponse res = null;
        if (!(token == null || token.trim().length() == 0)) {
            res = mapClient.getNearbyPlacesNextPage(token);
        } else {
            if (!(city == null || city.trim().length() == 0)) {
                double[] location = mapClient.getLocation(city);
                lat = location[0];
                lon = location[1];
            }
            res = mapClient.getPagedNearbyPlaces(lat,  lon);
        }


        log.info("query time: " + (LocalTime.now().getSecond() - localTime.getSecond() + 60) % 60);
        return res;
    }

    @GetMapping("/place/{placeId}")
    public Place getPlaceDetails(@PathVariable String placeId) {
        Place place = mapClient.getPlaceDetails(placeId);
        return place;
    }

    @PostMapping("/place")
    public List<Place> getPlacesDetails(@RequestBody List<String> placeIdList) {
        List<Place> places = placeRepository.findAllById(placeIdList);
        return places;
    }

    @GetMapping("/city")
    public City getCity() {
        City test = new City(Arrays.asList("culture", "modern"), Arrays.asList("walking", "driving", "bicycling", "transit"));
        return test;
    }


    @PostMapping("/city")
    public City recommendTour(@RequestBody City city) {
        city.setInterest(Arrays.asList("culture", "modern"));
        city.setTransportation(Arrays.asList("walking", "driving", "bicycling", "transit"));
        mapClient.fillCity(city);
        return city;
    }


}
