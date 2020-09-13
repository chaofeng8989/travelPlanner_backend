package team6.travelplanner.googleClient;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team6.travelplanner.Vault;
import team6.travelplanner.models.Photo;
import team6.travelplanner.models.PhotoRepository;
import team6.travelplanner.models.Place;
import team6.travelplanner.models.PlaceRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Component
public class MapClient {
    GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(Vault.googleAPIKEY)
            .build();

    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    PlaceRepository placeRepository;



    public Set<Place> getNearbyPlacesNextPage(String nextPageToken) {
        Set<Place> places = new HashSet<>();

        try {
            PlacesSearchResponse placesSearchResponse = PlacesApi
                                            .nearbySearchNextPage(context, nextPageToken)
                                            .await();
            for (PlacesSearchResult place : placesSearchResponse.results) {
                places.add(getPlaceDetails(place.placeId));
            }
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
                places.add(getPlaceDetails(place.placeId));
            }
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
        Place place = null;
        if (placeRepository.existsById(placeId)) {
            return placeRepository.getOne(placeId);
        }
        try {
            PlaceDetails placeDetails = null;
            placeDetails = PlacesApi.placeDetails(context, placeId).await();
            place = Place.getPlaceFromPlaceDetails(placeDetails);
            String photoReference = placeDetails.photos[0].photoReference;
            Photo photo = null;
            if (photoRepository.existsById(photoReference)) {
                photo = photoRepository.getOne(photoReference);
            } else {
                photo = new Photo();
                photo.setPhotoReference(photoReference);
                photo.setHeight(600);
                photo.setWidth(600);

                // save file to local, only for debug
                ImageResult r = getImage(photoReference, 600, 600);
                Files.write(Path.of("src/main/resources/"+placeDetails.name+".jpeg"), r.imageData);
                photoRepository.save(photo);
            }
            place.addPhoto(photo);
            placeRepository.save(place);
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return place;
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
    public String getImageURL(Photo photo) {

        return Photo.getURL(photo);
    }


    public static void main(String[] args) {
        MapClient client = new MapClient();
        Set<Place> response = client.getNearbyPlaces(47.608013,  -122.335167);
        System.out.println(response);
    }
}
