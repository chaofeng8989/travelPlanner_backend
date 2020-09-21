package team6.travelplanner.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class City {
    List<String> interest;
    List<String> transportation;

    String city;
    String state;

    double lat;
    double lon;
    public City(List<String> interest, List<String> transportation) {
        this.interest = interest;
        this.transportation = transportation;
    }
}
