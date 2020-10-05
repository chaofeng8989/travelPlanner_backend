package team6.travelplanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import team6.travelplanner.googleClient.MapClient;
import team6.travelplanner.models.City;
import team6.travelplanner.models.Place;
import team6.travelplanner.models.Tour;
import team6.travelplanner.repositories.TourRepository;

import java.util.List;

@SpringBootApplication
public class TravelplannerApplication {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(TravelplannerApplication.class, args);
	}
/*
	@Autowired TourRepository tourRepository;
	@Autowired
	MapClient mapClient;
	@Bean
	public void fillTour() {
		List<Tour> tours = tourRepository.findAll();
		for (Tour tour : tours) {

		}
		tourRepository.saveAll(tours);
	}
	*/
}
