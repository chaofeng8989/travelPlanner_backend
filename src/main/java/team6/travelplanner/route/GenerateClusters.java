package team6.travelplanner.route;

import team6.travelplanner.models.Place;
import team6.travelplanner.territorium.XYMinMaxQuantitiesHeterogeneousClusters;
import team6.travelplanner.territorium.optimiser.data.ImmutableSolution;
import team6.travelplanner.territorium.optimiser.solver.Solver;
import team6.travelplanner.territorium.optimiser.solver.SolverConfig;
import team6.travelplanner.territorium.problem.Customer;
import team6.travelplanner.territorium.problem.Problem;
import team6.travelplanner.territorium.problem.location.XYLocation;

import java.util.*;


public class GenerateClusters {

    public static List<List<Place>> clustering(List<Place> places, int clusterNumber) {

        List<Customer> cL = constructPlaceList(places);
        Problem problem = new XYMinMaxQuantitiesHeterogeneousClusters()
                .setNbCustomers(places.size())
                .setNbClusters(clusterNumber)
                .setTotalClusterCapacityMultiplier(1.05)
                .setTotalClusterMinQuantityMultiplier(0.95)
                .setCustomer(cL).build();

        Random random = new Random();
        Solver solver = new Solver(problem, new SolverConfig(), random);
        ImmutableSolution sol = solver.solve(null);

        List<List<Place>> clusters = new ArrayList<>();

//        System.out.println(sol);

        for (int i = 0; i < sol.getClusterCentres().length; i++) {

            List<Place> subCluster = new ArrayList<>();

            for (int j = 0; j < sol.getNbCustomers(i); j++) {
                int placeIndex = sol.getCustomer(i, j);
                subCluster.add(places.get(placeIndex));
            }

//            System.out.println(subCluster.size());

            clusters.add(subCluster);
        }

        return clusters;

    }

    private static List<Customer> constructPlaceList (List<Place> places){
        List<Customer> placeList = new ArrayList<>();
        int placeIndex = 0;
        for (Place p : places){
            Customer customer = new Customer();

            XYLocation xyLocation = new XYLocation();
			xyLocation.setIndex(placeIndex++);
            xyLocation.setX(p.getLat());
            xyLocation.setY(p.getLon());
            customer.setLocation(xyLocation);
            customer.setCostPerUnitDistance(1);
            placeList.add(customer);
        }
        return placeList;
    }

}