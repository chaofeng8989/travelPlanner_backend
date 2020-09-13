package team6.travelplanner.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Tour {
    @Id
    @GeneratedValue
    String id;

    @JoinColumn @OneToMany
    List<Place> placeList;

    @ElementCollection
    List<Long> placeTime;

    @ElementCollection
    Set<String> keywords;

    double rating;


}
