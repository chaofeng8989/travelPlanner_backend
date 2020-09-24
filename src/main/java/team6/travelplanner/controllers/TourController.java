package team6.travelplanner.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team6.travelplanner.models.Place;
import team6.travelplanner.models.PlaceRepository;
import team6.travelplanner.models.Tour;
import team6.travelplanner.models.TourRepository;
import team6.travelplanner.route.Cluster;
import team6.travelplanner.route.GenerateTour;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TourController {
    @Autowired private  PlaceRepository placeRepository;
    @Autowired private GenerateTour generateTour ;
    @Autowired private TourRepository tourRepository;

    @PostMapping("/tour/generate")
    public SimpleTour generateTour(@RequestBody RequestWrapper requestWrapper) {
        List<Place> places = placeRepository.findAllById(requestWrapper.placeIdSet);
        List<List<Place>> groups = Cluster.Cluster(places, requestWrapper.duration);
        for (int i = 0; i < groups.size(); i++) {
            System.out.println("cluster " + i + " : ");
            groups.get(i).stream().forEach(p -> System.out.print(p.getName() + " , "));
        }
        Tour tour = generateTour.solve(groups);
        tour.setDuration(requestWrapper.duration);
        tourRepository.save(tour);
        SimpleTour simpleTour = new SimpleTour(tour);
        return simpleTour;
    }

    @GetMapping("/tour/{tourId}")
    public Tour generateTourDetail(@PathVariable long tourId) {
        Tour tour = tourRepository.getOne(tourId);
        return tour;
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
        List<String> placeId;
        List<Integer> time;

        public SimpleDay(Tour.OneDayTour oneDayTour) {
            placeId = oneDayTour.getPlaceList().stream().map(Place::getName).collect(Collectors.toList());
            time = oneDayTour.getPlaceTime();
        }
    }

    @Data
    static class RequestWrapper{
        List<String> placeIdSet;
        int duration;
        String travelType;
    }

}
