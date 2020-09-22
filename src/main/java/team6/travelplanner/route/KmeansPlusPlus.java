package team6.travelplanner.route;

import org.apache.commons.math4.exception.DimensionMismatchException;
import org.apache.commons.math4.ml.clustering.CentroidCluster;
import org.apache.commons.math4.ml.clustering.Clusterable;
import org.apache.commons.math4.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math4.ml.distance.DistanceMeasure;

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
    public List<List<Place>> getKMeansResult(
            List<Place> places,
            Map<Place, Map<Place, Double>> distanceMap,
            int classNumber,
            int maxIterations){

        Map<String, Place> namePlaceMap = new HashMap<>();
        for (Map.Entry<Place, Map<Place, Double>> entry : distanceMap.entrySet()){
            if(!namePlaceMap.containsKey(entry.getKey().getPlaceId())){
                namePlaceMap.put(entry.getKey().getPlaceId(), entry.getKey());
            }
        }

        List<PlaceWrapper> clusterInput = new ArrayList<>(places.size());
        for (Place place : places){
            clusterInput.add(new PlaceWrapper(place));
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
        public Map<String, Place> namePlaceMap;
        public Distance (Map<Place, Map<Place, Double>> distanceMap, Map<String, Place> namePlaceMap){
            this.distanceMap = distanceMap;
            this.namePlaceMap = namePlaceMap;
        }

        @Override
        public double compute(double[] a, double[] b) throws DimensionMismatchException {
//            double value = 0.;
            Place p1 = namePlaceMap.get(String.valueOf(a[0]));
            Place p2 = namePlaceMap.get(String.valueOf(b[0]));
            return distanceMap.get(p1).get(p2);
        }

    }


    public static class PlaceWrapper implements Clusterable {
        private final Place place;
        private final double[] points;

        public PlaceWrapper(Place place) {
            this.place = place;
            this.points = new double[] { Double.parseDouble(place.getPlaceId()) };
        }

        public Place getPlace() {
            return place;
        }

        public double[] getPoint() {
            return points;
        }
    }


}