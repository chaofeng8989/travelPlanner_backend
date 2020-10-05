package team6.travelplanner.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.annotations.Target;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    String city;
    String state;

    int duration;

    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<OneDayTour> days = new LinkedList<>();

    @ElementCollection
    Set<String> keywords;

    double rating;

    @Entity
    @Data
    public static class OneDayTour{
        @JoinColumn @ManyToMany (cascade = CascadeType.ALL)

        List<Place> placeList;

        @ElementCollection
        List<Integer> placeTime;
        @Id @JsonIgnore
        @GeneratedValue (strategy = GenerationType.AUTO)
        private long id;

    }
}
