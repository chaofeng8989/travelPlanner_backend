package team6.travelplanner.models;


import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import team6.travelplanner.Vault;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@NoArgsConstructor
public class Photo {
    @Id
    String photoReference;

    @Lob
    String url;

    int width;
    int height;

    public Photo(com.google.maps.model.Photo photo) {
        url = "https://maps.googleapis.com/maps/api/place/photo?"
                +"&maxwidth=" + photo.width
                +"&maxheight=" + photo.height
                +"&photoreference=" + photo.photoReference
                +"&key=" + Vault.googleAPIKEY;
        photoReference = photo.photoReference;
        width = photo.width;
        height = photo.height;
    }

    @Override    @JsonValue

    public String toString() {
        return "Photo{" +
                "url='" + url + '\'' +
                '}';
    }
}
