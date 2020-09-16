package team6.travelplanner.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team6.travelplanner.googleClient.MapClient;
import team6.travelplanner.models.PagedResponse;
import team6.travelplanner.models.Place;

import java.time.LocalTime;

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

}
