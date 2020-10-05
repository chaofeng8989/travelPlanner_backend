package team6.travelplanner.controllers;

import com.google.maps.PlacesApi;
import com.google.maps.model.PlaceDetails;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team6.travelplanner.googleClient.MapClient;
import team6.travelplanner.models.City;
import team6.travelplanner.models.Place;
import team6.travelplanner.repositories.PlaceRepository;
import team6.travelplanner.models.Tour;
import team6.travelplanner.repositories.TourRepository;
import team6.travelplanner.route.GenerateClusters;
import team6.travelplanner.route.GenerateTour;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TourController {
    private final PlaceRepository placeRepository;
    private final GenerateTour generateTour ;
    private final TourRepository tourRepository;
    private final MapClient mapClient;
    @Autowired
    public TourController(PlaceRepository placeRepository, GenerateTour generateTour, TourRepository tourRepository, MapClient mapClient) {
        this.placeRepository = placeRepository;
        this.generateTour = generateTour;
        this.tourRepository = tourRepository;
        this.mapClient = mapClient;
    }

    @PostMapping("/tour/generate")
    public SimpleTour generateTour(@RequestBody RequestWrapper requestWrapper) {
        List<Place> places = placeRepository.findAllById(requestWrapper.placeIdSet);
        List<List<Place>> groups = GenerateClusters.clustering(places, requestWrapper.duration);
        for (int i = 0; i < groups.size(); i++) {
            System.out.println("cluster " + i + " : ");
            groups.get(i).stream().forEach(p -> System.out.print(p.getName() + " , "));
        }
        Tour tour = generateTour.solve(groups);
        tour.setDuration(requestWrapper.duration);
        fillTour(tour);
        tourRepository.save(tour);
        SimpleTour simpleTour = new SimpleTour(tour);
        return simpleTour;
    }


    @GetMapping("/tour/{tourId}")
    public Tour generateTourDetail(@PathVariable long tourId) {
        Tour tour = tourRepository.getOne(tourId);
        return tour;
    }


    public void fillTour(Tour tour) {
        Tour.OneDayTour oneDayTour = tour.getDays().get(0);
        Place p = oneDayTour.getPlaceList().get(0);
        String cityName = mapClient.getCityName(p.getPlaceId());
        tour.setCity(cityName);
    }

    @Data
    static class SimpleTour{
        List<SimpleDay> day;
        int duration;
        long id;
        public SimpleTour(Tour tour) {
            duration = tour.getDuration();
            id = tour.getId();
            day = tour.getDays().stream().map(SimpleDay::new).collect(Collectors.toList());
        }
    }

    @Data
    static class SimpleDay {
        List<SimplePlace> simplePlaces;

        List<Integer> time;

        public SimpleDay(Tour.OneDayTour oneDayTour) {
            simplePlaces = oneDayTour.getPlaceList().stream().map(SimplePlace::new).collect(Collectors.toList());
            time = oneDayTour.getPlaceTime();
        }
    }

    @Data static class SimplePlace {
        String name;
        String placeId;
        float rating;
        String photo;
        public SimplePlace(Place p) {
            name = p.getName();
            placeId = p.getPlaceId();
            rating = p.getRating();
            photo = p.getPhotos().iterator().next().toString();
        }
    }
    @Data
    static class RequestWrapper{
        List<String> placeIdSet;
        int duration;
        List<String> travelType;
    }

}
