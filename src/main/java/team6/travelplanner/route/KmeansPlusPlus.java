package team6.travelplanner.route;

import org.apache.commons.math4.exception.DimensionMismatchException;
import org.apache.commons.math4.ml.clustering.CentroidCluster;
import org.apache.commons.math4.ml.clustering.Clusterable;
import org.apache.commons.math4.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math4.ml.distance.DistanceMeasure;

import team6.travelplanner.googleClient.MapClient;
import team6.travelplanner.models.Place;

import java.util.*;

public class KmeansPlusPlus {
    /**
     * @author Administrator
     * @param places
     * @param distanceMap
     * @param classNumber
     * @param maxIterations
     */
    public List<List<Place>> getKMeansResult (
            List<Place> places,
            Map<Place, Map<Place, Double>> distanceMap,
            int classNumber,
            int maxIterations){

        Map<String, Integer> idCountMap = new HashMap<>();
        Map<Integer, Place> namePlaceMap = new HashMap<>();
        int count = 0;
        for (Place place: places) {
            if (!idCountMap.containsKey(place.getPlaceId())) {
                idCountMap.put(place.getPlaceId(), count);
                namePlaceMap.put(count, place);
                count++;
            }
        }

        List<PlaceWrapper> clusterInput = new ArrayList<>(places.size());
        for (Place place : places){
            clusterInput.add(new PlaceWrapper(place, idCountMap));
        }

        //   initialize a new clustering algorithm.
        Distance distance = new Distance(distanceMap, namePlaceMap);

        KMeansPlusPlusClusterer<PlaceWrapper> clusterer = new KMeansPlusPlusClusterer<>(classNumber, maxIterations, distance);
        List<CentroidCluster<PlaceWrapper>> clusterResults = clusterer.cluster(clusterInput);

        // output the clusters
        List<List<Place>> res = new ArrayList<>();
        for (CentroidCluster<PlaceWrapper> clusterResult : clusterResults) {
            List<Place> subRes = new ArrayList<>();
            for (PlaceWrapper locationWrapper : clusterResult.getPoints()) {
                subRes.add(locationWrapper.getPlace());
            }
            res.add(subRes);
        }
        return res;
    }

    public static class Distance implements DistanceMeasure {
        private static final long serialVersionUID = 1L;
        public Map<Place, Map<Place, Double>>  distanceMap;
        public Map<Integer, Place> namePlaceMap;
        public Distance (Map<Place, Map<Place, Double>> distanceMap, Map<Integer, Place> namePlaceMap){
            this.distanceMap = distanceMap;
            this.namePlaceMap = namePlaceMap;
        }

        @Override
        public double compute(double[] a, double[] b) throws DimensionMismatchException {
//            double value = 0.;
            Place p1 = namePlaceMap.get((int) a[0]);
            Place p2 = namePlaceMap.get((int) b[0]);
            return distanceMap.get(p1).get(p2);
        }

    }


    public static class PlaceWrapper implements Clusterable {
        private final Place place;
        private final double[] points;

        public PlaceWrapper(Place place, Map<String, Integer> map) {
            this.place = place;
            this.points = new double[] { (double) map.get(place.getPlaceId())};
        }

        public Place getPlace() {
            return place;
        }

        public double[] getPoint() {
            return points;
        }
    }


    public static void main(String... args) {
        MapClient mapClient = new MapClient();

        Set<Place> set = mapClient.getNearbyPlaces(47.608013, -122.335167);   //a set contains 20 places


        List<Place> places = new LinkedList<>();     //a list with 10 places

        for (Place place : set) {
//            if (places.size() >= 10) break;
                places.add(place);
        }


        Map<Place, Map<Place, Double>> dis = mapClient.getDistanceMatrix(places);   // distance matrix

//        for (int i = 0; i < places.size(); i++) {
//            for (int j = 0; j < places.size(); j++) {
//                System.out.print(dis.get(places.get(i)).get(places.get(j)) + " ");
//            }
//            System.out.println();
//        }

        GenerateClusters cluster = new GenerateClusters();
        System.out.println(cluster.clustering(places, 4));

//        KmeansPlusPlus kmp = new KmeansPlusPlus();
//        List<List<Place>> result = kmp.getKMeansResult(places, dis, 4, 10000);
//        for (List<Place> sub: result) {
//            System.out.print(sub.size());
//            System.out.println(sub);
//        }


    }

}