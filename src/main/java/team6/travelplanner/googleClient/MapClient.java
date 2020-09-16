package team6.travelplanner.googleClient;

import com.google.maps.GeoApiContext;
import com.google.maps.ImageResult;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team6.travelplanner.models.PagedResponse;
import team6.travelplanner.models.Place;
import team6.travelplanner.models.PlaceRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import static team6.travelplanner.Vault.GOOGLE_APIKEY;

@Component
@Slf4j
public class MapClient {
    GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(GOOGLE_APIKEY)
            .build();

    @Autowired
    PlaceRepository placeRepository;

    /**
     * @param nextPageToken
     * @return
     */
    public PagedResponse getNearbyPlacesNextPage(String nextPageToken) {
        PagedResponse res = new PagedResponse();
        Set<Place> places = new HashSet<>();
        try {
            PlacesSearchResponse placesSearchResponse = PlacesApi
                    .nearbySearchNextPage(context, nextPageToken)
                    .await();
            for (PlacesSearchResult place : placesSearchResponse.results) {
                places.add(Place.getPlaceFromPlacesSearchResult(place));
            }
            res.setEntity(places);
            res.setNextPageToken(nextPageToken);
            writeDatabase(places);
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public PagedResponse getNearbyPlaces(double lat, double lng) {
        PagedResponse res = new PagedResponse();
        Set<Place> places = new HashSet<>();
        try {
            PlacesSearchResponse placesSearchResponse = null;
            LatLng location = new LatLng(lat, lng);
            placesSearchResponse = PlacesApi.nearbySearchQuery(context, location)
                    .radius(5000).type(PlaceType.TOURIST_ATTRACTION).await();
            for (PlacesSearchResult place : placesSearchResponse.results) {
                places.add(Place.getPlaceFromPlacesSearchResult(place));
            }
            res.setEntity(places);
            res.setNextPageToken(placesSearchResponse.nextPageToken);
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

        return res;
    }

    public Place getPlaceDetails(String placeId) {
        Place place = placeRepository.getOne(placeId);
        try {
            PlaceDetails placeDetails = null;
            placeDetails = PlacesApi.placeDetails(context, placeId).await();
            log.info(placeDetails.toString());
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
