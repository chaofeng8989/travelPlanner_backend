package team6.travelplanner.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@JsonSerialize
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

    @OneToMany
    @JoinColumn

    Set<Photo> photos = new HashSet<>();
    public void addPhoto(Photo p) {
        photos.add(p);
    }

    public static Place getPlaceFromPlaceDetails(com.google.maps.model.PlaceDetails placeDetails) {
        Place place = new Place();
        place.formattedAddress = placeDetails.formattedAddress;
        place.formattedPhoneNumber = placeDetails.formattedPhoneNumber;
        place.icon = placeDetails.icon;
        place.internationalPhoneNumber = placeDetails.internationalPhoneNumber;
        place.name = placeDetails.name;
        place.placeId = placeDetails.placeId;
        place.userRatingsTotal = placeDetails.userRatingsTotal;
        place.rating = placeDetails.rating;
        place.vicinity = placeDetails.vicinity;
        place.website = placeDetails.website;
        //place.priceLevel = placeDetails.priceLevel.toString();
        return place;

    }

}
