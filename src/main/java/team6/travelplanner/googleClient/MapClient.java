package team6.travelplanner.googleClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.stereotype.Component;
import team6.travelplanner.Vault;

import java.io.IOException;

@Component
public class MapClient {
    GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(Vault.googleAPIKEY)
            .build();
    GeocodingResult[] results;

    {
        try {
            results = GeocodingApi.geocode(context,
                    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(results[0].addressComponents));
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
