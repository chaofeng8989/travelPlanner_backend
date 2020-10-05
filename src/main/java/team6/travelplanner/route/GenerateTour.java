package team6.travelplanner.route;

import com.google.maps.model.PlaceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team6.travelplanner.Constants;
import team6.travelplanner.googleClient.MapClient;
import team6.travelplanner.models.Place;
import team6.travelplanner.models.Tour;

import java.util.*;
import java.util.stream.Stream;

@Component
public class GenerateTour {
//    Map<Place, Map<Place, Integer>> time;
//    Map<Place, List<List<LocalTime>>> openHours;
@Autowired MapClient mapClient;


    public Tour solve(List<List<Place>> groups) {
        Tour tour = new Tour();
        for (List<Place> list : groups) {
            tour.getDays().add(new ShortestPath(list, mapClient).solve());
        }
        return tour;
    }


    class ShortestPath {
        MapClient mapClient;
        List<Place> result;
        List<Integer> times;
        double min = Double.MAX_VALUE;
        List<Place> list;
        Set<Place> set;
        int size;
        Map<Place, Map<Place, Double>> distance;

        public ShortestPath(List<Place> list, MapClient mapClient) {
            this.mapClient = mapClient;
            this.list = list;
            set = new HashSet<>(list);
            this.size = list.size();
            this.distance = mapClient.getDistanceMatrix(list);
        }

        public Tour.OneDayTour solve() {
            getOrder();
            Tour.OneDayTour oneDayTour = new Tour.OneDayTour();
            System.out.println("min duration is : "  + min);
            oneDayTour.setPlaceList(result);
            oneDayTour.setPlaceTime(times);
            return oneDayTour;
        }

        private void getOrder() {
            Set<Place> visited = new HashSet<>();
            LinkedList<Place> order = new LinkedList<>();
            LinkedList<Integer> startTime = new LinkedList<>();
            startTime.add(480);
            for (int i = 0; i < size; i++) {
                visited.add(list.get(i));
                order.add(list.get(i));
                dfs(list.get(i), 0, visited, order, startTime);
                visited.remove(list.get(i));
                order.removeLast();
            }
        }

        private void dfs(Place now, double preSum, Set<Place> visited, LinkedList<Place> pre, LinkedList<Integer> preTime) {
            if (visited.size() == size) {
                if (preSum < min) {
                    result = new LinkedList<>(pre);
                    times = new LinkedList<>(preTime);
                    min = preSum;
                    return;
                }
            }
            if (preSum >= min) {
                return;
            }
            for (Place next : list) {
                if (set.contains(next) && !visited.contains(next)) {
                    pre.add(next);
                    visited.add(next);
                    int visitTime = next.getType().stream().mapToInt(x -> Constants.AVERAGE_VISIT_TIME.getOrDefault(x, 60)).max().getAsInt();
                    preTime.add(distance.get(now).get(next).intValue() + visitTime + preTime.getLast());
                    dfs(next, preSum + distance.get(now).get(next), visited, pre, preTime);
                    visited.remove(next);
                    preTime.removeLast();
                    pre.removeLast();
                }
            }

        }
    }

}
