package team6.travelplanner.googleClient;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.FindPlaceFromText;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team6.travelplanner.controllers.PlaceController;
import team6.travelplanner.models.City;
import team6.travelplanner.models.Place;
import team6.travelplanner.repositories.PlaceRepository;

import java.io.IOException;
import java.util.*;

import static team6.travelplanner.Constants.GOOGLE_APIKEY;

@Component
@Slf4j
public class MapClient {
    GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(GOOGLE_APIKEY)
            .build();

    @Autowired
    PlaceRepository placeRepository;


    public PlaceController.PagedResponse getNearbyPlacesNextPage(String nextPageToken) {
        PlaceController.PagedResponse res = new PlaceController.PagedResponse();
        Set<Place> places = new HashSet<>();
        try {
            PlacesSearchResponse placesSearchResponse = PlacesApi
                    .nearbySearchNextPage(context, nextPageToken)
                    .await();
            for (PlacesSearchResult place : placesSearchResponse.results) {
                places.add(Place.getPlaceFromPlacesSearchResult(place));
            }
            res.setEntity(places);
            res.setNextPageToken(placesSearchResponse.nextPageToken);
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

    public PlaceController.PagedResponse getPagedNearbyPlaces(double lat, double lng) {
        PlaceController.PagedResponse res = new PlaceController.PagedResponse();
        Set<Place> places = new HashSet<>();
        try {
            LatLng location = new LatLng(lat, lng);
            PlacesSearchResponse placesSearchResponse = PlacesApi.nearbySearchQuery(context, location)
                    .radius(5000).type(PlaceType.TOURIST_ATTRACTION).await();
            for (PlacesSearchResult place : placesSearchResponse.results) {
                places.add(Place.getPlaceFromPlacesSearchResult(place));
            }
            res.setEntity(places);
            res.setNextPageToken(placesSearchResponse.nextPageToken);
            log.info("nearby places", places);
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
        Place place = null;
        if (placeRepository.existsById(placeId)) {
            place = placeRepository.getOne(placeId);
        } else {
            place = new Place();
        }

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

    public String getCityName(String placeId) {
        try {
            PlaceDetails placeDetails = PlacesApi.placeDetails(context, placeId).await();
            String address = placeDetails.formattedAddress;
            return address.split(",")[1].trim();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void fillCity(City city) {
        try {
            FindPlaceFromText response =  PlacesApi.findPlaceFromText(context, city.getCity()+", " + city.getState(), FindPlaceFromTextRequest.InputType.TEXT_QUERY).fields(FindPlaceFromTextRequest.FieldMask.GEOMETRY).await();
            if (response.candidates.length == 0) {
                log.error("city validation error");
            } else {
                PlacesSearchResult result = response.candidates[0];
                log.info("city info : " + result);
                city.setLat(result.geometry.location.lat);
                city.setLon(result.geometry.location.lng);
            }
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeDatabase(Set<Place> places) {
        new Thread(()->
                places.forEach(place -> {
                    if (!placeRepository.existsById(place.getPlaceId())) {
                        placeRepository.save(place);
                    }
                })
            ).start();
    }

    private void writeDatabase(Place place) {
        new Thread(() -> placeRepository.save(place)).start();
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




    public Set<Place> getNearbyPlaces(double lat, double lng) {
        Set<Place> places = new HashSet<>();
        try {
            LatLng location = new LatLng(lat, lng);
            PlacesSearchResponse placesSearchResponse = PlacesApi.nearbySearchQuery(context, location)
                    .radius(15000).type(PlaceType.TOURIST_ATTRACTION).await();
            for (PlacesSearchResult place : placesSearchResponse.results) {
                places.add(Place.getPlaceFromPlacesSearchResult(place));
            }
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return places;
    }


    public Map<Place, Map<Place, Double>> getDistanceMatrix(List<Place> places) {
        Map<Place, Map<Place, Double>> distanceMatrix = new HashMap<>();
        String[] origins = places.stream().map(place -> "place_id:"+place.getPlaceId()).toArray(String[]::new);
        String[] destinations = places.stream().map(place -> "place_id:"+place.getPlaceId()).toArray(String[]::new);
        System.out.println("total size : " + origins.length);
        Arrays.stream(origins).forEach(s -> System.out.print(s+"|"));
        System.out.println();
        try {
            DistanceMatrix matrix = DistanceMatrixApi.getDistanceMatrix(context, origins, destinations).await();
            for (int i = 0; i < places.size(); i++) {
                Place source = places.get(i);
                distanceMatrix.put(source, new HashMap<>());
                for (int j = 0; j < places.size(); j++) {
                    Place destination = places.get(j);
                    distanceMatrix.get(source).put(destination, 1.0 * matrix.rows[i].elements[j].duration.inSeconds / 60);
                }
            }
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return distanceMatrix;
    }

    public double[] getLocation(String city) {
        try {
            GeocodingResult[] results = GeocodingApi.newRequest(context).address(city).await();
            if (results.length == 0) throw new IllegalArgumentException("wrong city address");
            Geometry geometry = results[0].geometry;

            return new double[]{geometry.location.lat, geometry.location.lng};
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new double[]{0.0, 0.0};
    }

}
