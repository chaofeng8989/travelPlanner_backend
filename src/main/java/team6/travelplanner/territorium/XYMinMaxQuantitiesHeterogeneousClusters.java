package team6.travelplanner.territorium;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

import team6.travelplanner.territorium.optimiser.data.ImmutableSolution;
import team6.travelplanner.territorium.optimiser.solver.Solver;
import team6.travelplanner.territorium.optimiser.solver.SolverConfig;
import team6.travelplanner.territorium.optimiser.solver.SolverStateSummary;
import team6.travelplanner.territorium.problem.ArrayBasedTravelMatrix;
import team6.travelplanner.territorium.problem.Cluster;
import team6.travelplanner.territorium.problem.Customer;
import team6.travelplanner.territorium.problem.Problem;
import team6.travelplanner.territorium.problem.location.XYLocation;
import team6.travelplanner.territorium.utils.StringUtils;

public class XYMinMaxQuantitiesHeterogeneousClusters {
	private static final Logger LOGGER = Logger.getLogger(XYMinMaxQuantitiesHeterogeneousClusters.class.getName());
	private int nbCustomers=100;
	private int nbClusters=10;
	private double maxCustomerQuantity=100;
	private double minCustomerQuantity=1;
	private int customerQuantityDistributionRandPower=3;
	private double totalClusterCapacityMultiplier = 1.2;
	private double totalClusterMinQuantityMultiplier = 0.8;
	private List<Customer> customerList = null;
	
	public XYMinMaxQuantitiesHeterogeneousClusters setNbCustomers(int nb){
		nbCustomers = nb;
		return this;
	}

	
	public XYMinMaxQuantitiesHeterogeneousClusters setCustomerQuantityDistributionRandPower(int quantityDistributionRandPower) {
		this.customerQuantityDistributionRandPower = quantityDistributionRandPower;
		return this;
	}


	public XYMinMaxQuantitiesHeterogeneousClusters setTotalClusterCapacityMultiplier(double totalClusterCapacityMultiplier) {
		this.totalClusterCapacityMultiplier = totalClusterCapacityMultiplier;
		return this;
	}


	public XYMinMaxQuantitiesHeterogeneousClusters setTotalClusterMinQuantityMultiplier(double totalClusterMinQuantityMultiplier) {
		this.totalClusterMinQuantityMultiplier = totalClusterMinQuantityMultiplier;
		return this;
	}


	public XYMinMaxQuantitiesHeterogeneousClusters setNbClusters(int nb){
		nbClusters = nb;
		return this;
	}
	
	
	public XYMinMaxQuantitiesHeterogeneousClusters setMaxCustomerQuantity(double val){
		maxCustomerQuantity = val;
		return this;
	}

	public XYMinMaxQuantitiesHeterogeneousClusters setMinCustomerQuantity(double val){
		minCustomerQuantity = val;
		return this;
	}

	public  XYMinMaxQuantitiesHeterogeneousClusters setCustomer(List<Customer> list){
		customerList = list;
		return this;
	}

	public Problem build(){
		// generate customers with lots of variation in quantity
		Problem problem = new Problem();
		double sumQuantity=0;

		for (Customer c : customerList){
			sumQuantity += c.getQuantity();
			problem.getCustomers().add(c);
		}

		// create input clusters
		double totalCapacity = sumQuantity * totalClusterCapacityMultiplier;
		double totalMin = sumQuantity * totalClusterMinQuantityMultiplier;
		for(int i =0 ; i< nbClusters ; i++){
			Cluster cluster = new Cluster();
			cluster.setMinQuantity(totalMin/nbClusters);
			cluster.setMaxQuantity(totalCapacity/nbClusters);
			problem.getClusters().add(cluster);
		}

		// create matrix
		ArrayBasedTravelMatrix matrix =ArrayBasedTravelMatrix.buildForXYProblem(problem, 1);
		problem.setTravelMatrix(matrix);
		
		return problem;
	}

	/**
	 * Test main function
	 * @param args
	 */
	public static void main(String []args){
		StringUtils.setOneLineLogging();
		
		// keep same problem
		Random random = new Random(100);
		Problem problem = new XYMinMaxQuantitiesHeterogeneousClusters().setNbCustomers(15).setNbClusters(4).setTotalClusterCapacityMultiplier(1.05).setTotalClusterMinQuantityMultiplier(0.95).build();
	
		// seed solver differently each time
		random = new Random();
		Solver solver = new Solver(problem, new SolverConfig(), random);
		
		ImmutableSolution sol = solver.solve(null);
		System.out.println(sol);
		
	}
}