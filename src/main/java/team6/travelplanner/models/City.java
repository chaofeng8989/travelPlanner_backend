package team6.travelplanner.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class City {
    List<String> interest;
    List<String> transportation;
    List<Long> recommendTourId;
    String city;
    String state;

    double lat;
    double lon;
    public City(List<String> interest, List<String> transportation) {
        this.interest = interest;
        this.transportation = transportation;
    }
}
