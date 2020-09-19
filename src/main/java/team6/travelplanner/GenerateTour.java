package team6.travelplanner;

import team6.travelplanner.models.Place;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class GenerateTour {
    Map<Place, Map<Place, Integer>> time;
    Map<Place, Map<Place, Integer>> distance;
    Map<Place, List<List<LocalTime>>> openHours;
    List<List<Place>> groups;
    public GenerateTour(Map<Place, Map<Place, Integer>> time, Map<Place, Map<Place, Integer>> distance, Map<Place, List<List<LocalTime>>> openHours) {
        this.time = time;
        this.distance = distance;
        this.openHours = openHours;
    }

    private OneDayTour astar() {

        return null;
    }
    public static class OneDayTour{
        List<Place> placeList;
        List<LocalTime> time;
    }
}
