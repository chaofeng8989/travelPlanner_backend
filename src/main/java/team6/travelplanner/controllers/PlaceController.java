package team6.travelplanner.controllers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team6.travelplanner.googleClient.MapClient;
import team6.travelplanner.models.City;
import team6.travelplanner.models.Place;
import team6.travelplanner.models.Tour;
import team6.travelplanner.repositories.PlaceRepository;
import team6.travelplanner.repositories.TourRepository;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class PlaceController {
    private final MapClient mapClient;
    private final PlaceRepository placeRepository;
    private final TourRepository tourRepository;
    @Autowired
    public PlaceController(MapClient mapClient, PlaceRepository placeRepository, TourRepository tourRepository) {
        this.mapClient = mapClient;
        this.placeRepository = placeRepository;
        this.tourRepository = tourRepository;
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
        City test = new City(Arrays.asList("culture", "modern"), Arrays.asList("Walking", "Driving", "Bicycling", "Transit"));
        return test;
    }


    @PostMapping("/city")
    public City recommendTour(@RequestBody City city) {
        city.setInterest(Arrays.asList("culture", "modern"));
        city.setTransportation(Arrays.asList("Walking", "Driving", "Bicycling", "Transit"));
        mapClient.fillCity(city);
        List<Tour> tours = tourRepository.findAllByCity(city.getCity());
        if (tours.size() > 5) tours = tours.subList(0, 5);
        List<Long> tourIds = tours.stream().map(Tour::getId).collect(Collectors.toList());
        city.setRecommendTourId(tourIds);
        return city;
    }


    @Data
    public static class PagedResponse {
        Set<Place> entity = new HashSet<>();
        String nextPageToken;
    }


}
