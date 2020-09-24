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
    @GeneratedValue
    long id;

    int duration;

    @OneToMany (cascade = CascadeType.ALL)
    List<OneDayTour> days = new LinkedList<>();

    @ElementCollection
    Set<String> keywords;

    double rating;

    @Entity
    @Data
    public static class OneDayTour{
        @JoinColumn @OneToMany
        List<Place> placeList;

        @ElementCollection
        List<Integer> placeTime;
        @Id @JsonIgnore
        @GeneratedValue
        private long id;

    }
}
