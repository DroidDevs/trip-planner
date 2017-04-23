package droiddevs.com.tripplanner.model.source.remote;

/**
 * Created by elmira on 4/12/17.
 */

public interface FbJsonAttributes {

    String DATA = "data";

    interface User {
        String NAME = "name";
        String ID = "id";
        String COVER = "cover";
        String PICTURE = "picture";
        String HOMETOWN = "hometown";
        String LOCATION = "location";
        String EMAIL = "email";
    }

    interface CoverPhoto {
        String SOURCE = "source";
        String ID = "id";
    }

    interface ProfilePictureSource {
        String URL = "url";
        String WIDTH = "width";
        String HEIGHT = "height";
    }
}
