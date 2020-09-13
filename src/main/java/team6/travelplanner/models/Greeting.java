package team6.travelplanner.models;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@RequiredArgsConstructor
public class Greeting {
    @NonNull
    private  String content;

    @Id
    @GeneratedValue
    private long id;
}