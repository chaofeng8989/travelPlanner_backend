package team6.travelplanner.route;


import org.apache.commons.math4.ml.clustering.CentroidCluster;
import org.apache.commons.math4.ml.clustering.Clusterable;
import org.apache.commons.math4.ml.clustering.KMeansPlusPlusClusterer;
import team6.travelplanner.models.Place;

import java.util.*;


public class Cluster {

    public static List<List<Place>> Cluster(List<Place> places, int clusterNumber) {
        List<PlaceWrapper> clusterInput = new ArrayList<PlaceWrapper>(places.size());
        for (Place location : places){
            clusterInput.add(new PlaceWrapper(location));
        }
        // initialize a new clustering algorithm.
        // we use KMeans++ with 10 clusters and 10000 iterations maximum.
        // we did not specify a distance measure; the default (euclidean distance) is used.
        KMeansPlusPlusClusterer<PlaceWrapper> clusterer = new KMeansPlusPlusClusterer<PlaceWrapper>(clusterNumber, 10000);
        List<CentroidCluster<PlaceWrapper>> clusterResults = clusterer.cluster(clusterInput);

        List<List<Place>> groups = new LinkedList<>();
        // output the clusters
        for (int i=0; i<clusterResults.size(); i++) {
            List<Place> tmp = new LinkedList<>();
            for (PlaceWrapper locationWrapper : clusterResults.get(i).getPoints()) {
                tmp.add(locationWrapper.getPlace());
            }
            groups.add(tmp);
        }
        return groups;
    }

    public static class PlaceWrapper implements Clusterable {
        private double[] points;
        private Place place;

        public PlaceWrapper(Place place) {
            this.place = place;
            this.points = new double[] { place.getLat(), place.getLon() };
        }

        public Place getPlace() {
            return place;
        }

        public double[] getPoint() {
            return points;
        }
    }

}