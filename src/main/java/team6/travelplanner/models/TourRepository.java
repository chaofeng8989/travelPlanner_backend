package team6.travelplanner.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TourRepository extends JpaRepository<Tour, String> {
}