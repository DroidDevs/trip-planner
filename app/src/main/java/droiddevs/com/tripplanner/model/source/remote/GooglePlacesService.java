package droiddevs.com.tripplanner.model.source.remote;

import java.util.List;

import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by elmira on 4/12/17.
 */

public interface GooglePlacesService {

    String ENDPOINT = "https://maps.googleapis.com/maps/api/place/";

    @GET("nearbysearch/json")
    Call<List<GooglePlace>> searchPlaces(@Query("location") String location,
                                         @Query("radius") int radius,
                                         @Query("type") String type,
                                         @Query("key") String apiKey);


    @GET("details/json")
    Call<PlaceDetailsResponse> getPlaceDetails(@Query("placeid") String placeId,
                                               @Query("key") String apiKey);
}
