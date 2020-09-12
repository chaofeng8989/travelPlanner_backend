package team6.travelplanner.googleClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.springframework.stereotype.Component;
import team6.travelplanner.Vault;
import team6.travelplanner.models.Place;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MapClient {
    GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(Vault.googleAPIKEY)
            .build();

    public PlacesSearchResponse getNearbyPlacesNextPage(String nextPageToken) {
        PlacesSearchResponse result = null;
        try {

            result = PlacesApi.nearbySearchNextPage(context, nextPageToken)
                    .await();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(result));
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public PlacesSearchResponse getNearbyPlaces(double lat, double lng) {
        PlacesSearchResponse result = null;

        try {

            LatLng location = new LatLng(lat, lng);
            result = PlacesApi.nearbySearchQuery(context, location)
                    .radius(5000)
                    .type(PlaceType.AMUSEMENT_PARK)
                    .type(PlaceType.AQUARIUM)
                    .type(PlaceType.ART_GALLERY)
                    .type(PlaceType.TOURIST_ATTRACTION)
                    .await();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(result));
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public PlaceDetails getPlaceDetails(String placeId) {
        PlaceDetails placeDetails = null;
        try {
             placeDetails = PlacesApi.placeDetails(context, placeId).await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(placeDetails);
        return placeDetails;
    }
    final String photoReference = "Photo Reference";

    public ImageResult getPhotoUrl(String photoReference, int width, int height) {
        ImageResult p = null;
        try {
            p = PlacesApi.photo(context, photoReference)
                    .maxWidth(width)
                    .maxHeight(height)
                    .await();
            System.out.println("    " + p);
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    public static void main(String[] args) {
        MapClient client = new MapClient();
        PlacesSearchResponse response = client.getNearbyPlaces(47.608013,  -122.335167);
        System.out.println(response.nextPageToken);
        Photo photo = null;

        for (PlacesSearchResult p : response.results) {
            client.getPlaceDetails(p.placeId);
            ImageResult r = client.getPhotoUrl(p.photos[0].photoReference, 400, 400);
            try {
                Files.write(Path.of("src/main/resources/"+p.name+".jpg"), r.imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
