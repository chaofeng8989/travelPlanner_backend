package team6.travelplanner.models;

import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResult;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Place {
    @Id
    String placeId;
    PlacesSearchResult placesSearchResult;
    PlaceDetails placeDetails;

}
