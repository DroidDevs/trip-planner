package droiddevs.com.tripplanner.model.source.remote;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by elmira on 4/12/17.
 */

public class RetrofitGooglePlacesService {

    public static GooglePlacesService newGooglePlacesService() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapter(new TypeToken<List<GooglePlace>>(){}.getType(), new GooglePlaceDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GooglePlacesService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(GooglePlacesService.class);
    }
}
