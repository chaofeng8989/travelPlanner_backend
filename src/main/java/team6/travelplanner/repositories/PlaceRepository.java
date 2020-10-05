package team6.travelplanner.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team6.travelplanner.models.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, String> {
}
