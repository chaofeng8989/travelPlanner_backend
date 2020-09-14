package team6.travelplanner.googleClient;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team6.travelplanner.Vault;
import team6.travelplanner.models.Place;
import team6.travelplanner.models.PlaceRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class MapClient {
    GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(Vault.googleAPIKEY)
            .build();

    @Autowired
    PlaceRepository placeRepository;



    public Set<Place> getNearbyPlacesNextPage(String nextPageToken) {
        Set<Place> places = new HashSet<>();

        try {
            PlacesSearchResponse placesSearchResponse = PlacesApi
                                            .nearbySearchNextPage(context, nextPageToken)
                                            .await();
            for (PlacesSearchResult place : placesSearchResponse.results) {
                places.add(Place.getPlaceFromPlacesSearchResult(place));
            }

            System.out.println(placesSearchResponse.nextPageToken);
            writeDatabase(places);
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return places;
    }


    public Set<Place> getNearbyPlaces(double lat, double lng) {
        Set<Place> places = new HashSet<>();

        try {
            PlacesSearchResponse placesSearchResponse = null;

            LatLng location = new LatLng(lat, lng);
            placesSearchResponse = PlacesApi.nearbySearchQuery(context, location)
                    .radius(5000)
                    .type(PlaceType.AMUSEMENT_PARK)
                    .type(PlaceType.AQUARIUM)
                    .type(PlaceType.ART_GALLERY)
                    .type(PlaceType.TOURIST_ATTRACTION)
                    .await();
            for (PlacesSearchResult place : placesSearchResponse.results) {
                places.add(Place.getPlaceFromPlacesSearchResult(place));
            }
            System.out.println(places);
            writeDatabase(places);
            System.out.println(placesSearchResponse.nextPageToken);
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return places;
    }

    public Place getPlaceDetails(String placeId) {
        Place place = placeRepository.getOne(placeId);
        try {
            PlaceDetails placeDetails = null;
            placeDetails = PlacesApi.placeDetails(context, placeId).await();
            System.out.println(placeDetails);
            place.addDetails(placeDetails);
            writeDatabase(place);
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return place;
    }

    private void writeDatabase(Set<Place> places) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                places.stream().forEach(place -> placeRepository.save(place));
            }
        }).start();
    }
    private void writeDatabase(Place place) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                placeRepository.save(place);
            }
        }).start();
    }

    public ImageResult getImage(String photoReference, int width, int height) {
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


}
