package team6.travelplanner.models;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class PagedResponse {
    Set<Place> entity = new HashSet<>();
    String nextPageToken;
}
