package team6.travelplanner.models;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.maps.model.PlacesSearchResult;
import lombok.Data;
import org.springframework.data.jpa.repository.Modifying;
import team6.travelplanner.Vault;

import javax.persistence.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Place {
    String formattedAddress;
    String formattedPhoneNumber;
    URL icon;
    String internationalPhoneNumber;
    String name;
    @Id
    String placeId;

    int userRatingsTotal;
    String priceLevel;
    float rating;
    String vicinity;
    URL website;


    @JoinColumn @OneToMany(cascade = CascadeType.ALL) @JsonSerialize
    Set<Photo> photos = new HashSet<>();


    public void addDetails(com.google.maps.model.PlaceDetails placeDetails) {
        formattedAddress = placeDetails.formattedAddress;
        formattedPhoneNumber = placeDetails.formattedPhoneNumber;
        icon = placeDetails.icon;
        internationalPhoneNumber = placeDetails.internationalPhoneNumber;
        name = placeDetails.name;
        placeId = placeDetails.placeId;
        userRatingsTotal = placeDetails.userRatingsTotal;
        rating = placeDetails.rating;
        vicinity = placeDetails.vicinity;
        website = placeDetails.website;
        for (com.google.maps.model.Photo photo : placeDetails.photos) {
            photos.add(new Photo(photo));
        }
    }

    public static Place getPlaceFromPlacesSearchResult(PlacesSearchResult result) {
        Place place = new Place();
        place.formattedAddress = result.formattedAddress;
        place.name = result.name;
        place.icon = result.icon;
        place.placeId = result.placeId;
        place.rating = result.rating;
        for (com.google.maps.model.Photo photo : result.photos) {
            place.photos.add(new Photo(photo));
        }
        return place;
    }

}
