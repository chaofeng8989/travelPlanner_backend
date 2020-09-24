package team6.travelplanner;

import com.google.maps.model.PlaceType;

import java.util.HashMap;
import java.util.Map;


public class Constants {
    public static final String GOOGLE_APIKEY = "AIzaSyB5Aextt4PuSqpd0F0_fHMY95iTZYA5OkY";
    public static final Map<String, Integer> AVERAGE_VISIT_TIME;

    static {
        AVERAGE_VISIT_TIME = new HashMap<>();
        for (PlaceType type : PlaceType.values()) {
            AVERAGE_VISIT_TIME.put(type.name(), 60);
        }
        AVERAGE_VISIT_TIME.put(PlaceType.AMUSEMENT_PARK.name(), 120);
        AVERAGE_VISIT_TIME.put(PlaceType.ART_GALLERY.name(), 120);
        AVERAGE_VISIT_TIME.put(PlaceType.MUSEUM.name(), 120);
        AVERAGE_VISIT_TIME.put(PlaceType.ZOO.name(), 120);

    };
}
