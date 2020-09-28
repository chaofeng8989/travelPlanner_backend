package team6.travelplanner.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {
    @GetMapping("/")
    public String root() {
        return "try /place/search?lat={lat}&lon={lon} to get nearby places\n"
                +"/place/placeId to get placeDetails\n"
                +"/place/search?nextPageToken={nextPageToken} to getnextpage";
    }
}
