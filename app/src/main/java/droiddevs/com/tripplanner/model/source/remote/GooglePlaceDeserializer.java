package droiddevs.com.tripplanner.model.source.remote;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;

/**
 * Created by Jared12 on 4/16/17.
 */

public class GooglePlaceDeserializer implements JsonDeserializer<List<GooglePlace>> {
    @Override
    public List<GooglePlace> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        JsonObject jsonObj = json.getAsJsonObject();
        JsonArray placesArray = jsonObj.get("results").getAsJsonArray();

        List<GooglePlace> placesList = gson.fromJson(placesArray, new TypeToken<List<GooglePlace>>(){}.getType());
        return placesList;
    }
}
