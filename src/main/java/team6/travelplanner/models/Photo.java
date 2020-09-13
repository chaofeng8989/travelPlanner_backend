package team6.travelplanner.models;

import com.google.maps.ImageResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import team6.travelplanner.Vault;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Photo {
    @Id
    String photoReference;
    static final String photoAddress = "https://maps.googleapis.com/maps/api/place/photo?";

    int height;
    int width;

    public static String getURL(Photo photo) {
        return photoAddress
                    +"&maxwidth=" + photo.width
                    +"&maxheight=" + photo.height
                    +"&photoreference=" + photo.photoReference
                    +"&key=" + Vault.googleAPIKEY;
    }
}
