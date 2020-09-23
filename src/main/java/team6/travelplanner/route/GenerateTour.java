package team6.travelplanner.route;

import team6.travelplanner.googleClient.MapClient;
import team6.travelplanner.models.Place;

import java.util.*;

public class GenerateTour {
//    Map<Place, Map<Place, Integer>> time;
//    Map<Place, List<List<LocalTime>>> openHours;
    List<List<Place>> groups;
    MapClient mapClient;
    public GenerateTour(
//                      Map<Place, Map<Place, Integer>> time,
//                        Map<Place, List<List<LocalTime>>> openHours,
                        MapClient mapclient,
                        List<List<Place>> groups) {
//        this.time = time;
        this.groups = groups;
        this.mapClient = mapclient;
//        this.openHours = openHours;
    }
    public List<List<Place>> solve() {
        List<List<Place>> result = new LinkedList<>();
        for (List<Place> list : groups) {
            result.add(new ShortestPath(list).solve());
        }
        return result;
    }








     class ShortestPath {
        List<Place> result;
        double min = Double.MAX_VALUE;
        List<Place> list;
        Set<Place> set;
        int size;
        Map<Place, Map<Place, Double>> distance;

        public ShortestPath(List<Place> list) {
            this.list = list;
            set = new HashSet<>(list);
            this.size = list.size();
            this.distance = mapClient.getDistanceMatrix(list);
        }

        public List<Place> solve() {
            getOrder();
            System.out.println("min distance is : "  + min);
            return result;
        }

        private void getOrder() {
            Set<Place> visited = new HashSet<>();
            LinkedList<Place> order = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                visited.add(list.get(i));
                order.add(list.get(i));
                dfs(list.get(i), 0, visited, order);
                visited.remove(list.get(i));
                order.removeLast();
            }
        }

        private void dfs(Place now, double preSum, Set<Place> visited, LinkedList<Place> pre) {
            if (visited.size() == size) {
                if (preSum < min) {
                    result = new LinkedList<>(pre);
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
                    dfs(next, preSum + distance.get(now).get(next), visited, pre);
                    visited.remove(next);
                    pre.removeLast();
                }
            }

        }
    }

    public static void main(String... args) {
        MapClient mapClient = new MapClient();

        Set<Place> set = mapClient.getNearbyPlaces(47.608013, -122.335167);
        List<Place> places1 = new LinkedList<>();
        List<Place> places2 = new LinkedList<>();
        List<Place> places = new LinkedList<>();

        for (Place place : set) {
            places.add(place);
            if (places1.size() < 10) places1.add(place);
            else if(places2.size() < 10)places2.add(place);
            if (places2.size() == 10) break;
        }

        List<List<Place>> groups = Arrays.asList(places1, places2);
        GenerateTour shortestPath = new GenerateTour(mapClient, groups);
        List<List<Place>> orders = shortestPath.solve();


        orders.forEach(x ->{
            System.out.println("=============\n");

            x.stream().forEach(place -> System.out.print("  --->" + place.getName()));
        });


//        KmeansPlusPlus kmeansPlusPlus = new KmeansPlusPlus();
//        Map<Place, Map<Place, Double>> dis = mapClient.getDistanceMatrix(places1);
//        List<List<Place>> kMeansResult = kmeansPlusPlus.getKMeansResult(places1, dis, 3, 20);
//        for (List<Place> placeList : kMeansResult) {
//            for (Place p : placeList) {
//                System.out.print(p.getName() + ",    ");
//            }
//            System.out.println();
//        }

    }
}
