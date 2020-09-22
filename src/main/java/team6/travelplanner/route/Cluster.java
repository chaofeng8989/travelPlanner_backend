package team6.travelplanner.route;


import team6.travelplanner.models.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Cluster {

    public List<List<Place>> a(Map<Place, Map<Place, Integer>> distanceMap, int duration) {
        List<List<Place>> clusteredPlaces = new ArrayList<>();


        Set<Place> allPlaces = distanceMap.keySet();

        for (Place place : allPlaces) {
            double lat = place.getLat();
            double lon = place.getLon();
        }

//        DbScan dbScan = new DbScan();
//        Map<NodePair, Double> nodesPairMap = dbScan.getCodingFileMap(inputPath);
//        dbscan.getDBSCANResult(locations, nodesPairMap,0.5,10);
//        clusteredPlaces.add();

        //    public static void main(String[] args) throws IOException {
//        // TODO Auto-generated method stub
//        KmeansPlusPlus kmeans = new KmeansPlusPlus();
//        Map<NodePair, Double> nodesPairMap = kmeans.getCodingFileMap(inputPath);
//        kmeans.getKMeansResult(locations, nodesPairMap,2,10000);
//    }

        return clusteredPlaces;
    }
}