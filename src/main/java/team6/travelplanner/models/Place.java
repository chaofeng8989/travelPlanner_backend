package team6.travelplanner.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.maps.model.PlacesSearchResult;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Place {
    String formattedAddress;
    String formattedPhoneNumber;
    URL icon;
    String internationalPhoneNumber;
    String name;
    @Id
    String placeId;

    double lat;
    double lon;

    @ElementCollection
    Set<String> type;
    int userRatingsTotal;
    String priceLevel;
    float rating;
    String vicinity;
    URL website;


    @JoinColumn @OneToMany(cascade = CascadeType.ALL)
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
        lat = placeDetails.geometry.location.lat;
        lon = placeDetails.geometry.location.lng;
        for (com.google.maps.model.Photo photo : placeDetails.photos) {
            photos.add(new Photo(photo));
        }
    }

    public static Place getPlaceFromPlacesSearchResult(@NonNull  PlacesSearchResult result) {
        Place place = new Place();
        place.type = Arrays.stream(result.types).collect(Collectors.toSet());
        place.formattedAddress = result.formattedAddress;
        place.name = result.name;
        place.icon = result.icon;
        place.placeId = result.placeId;
        place.rating = result.rating;
        place.lat = result.geometry.location.lat;
        place.lon = result.geometry.location.lng;
        if (result.photos != null) {
            for (com.google.maps.model.Photo photo : result.photos) {
                place.photos.add(new Photo(photo));
            }
        }
        return place;
    }
}
