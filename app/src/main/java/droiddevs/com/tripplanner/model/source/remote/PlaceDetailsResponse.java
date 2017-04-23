package droiddevs.com.tripplanner.model.source.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import droiddevs.com.tripplanner.util.PhotoUrlUtil;

/**
 * Created by elmira on 4/13/17.
 */

public class PlaceDetailsResponse {

    @SerializedName("result")
    PlaceDetails placeDetails;

    public PlaceDetails getPlaceDetails() {
        return placeDetails;
    }

    public void setPlaceDetails(PlaceDetails placeDetails) {
        this.placeDetails = placeDetails;
    }

    public static class PlaceDetails {

        @SerializedName("place_id")
        String placeId;

        @SerializedName("name")
        String name;

        @SerializedName("icon")
        String iconUrl;

        @SerializedName("rating")
        double rating;

        @SerializedName("geometry")
        Geometry geometry;

        @SerializedName("formatted_address")
        String address;

        @SerializedName("international_phone_number")
        String phoneNumber;

        @SerializedName("photos")
        List<PlacePhoto> photos;

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public void setPhotos(List<PlacePhoto> photos) {
            this.photos = photos;
        }

        public String getPlaceId() {
            return placeId;
        }

        public String getName() {
            return name;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public double getRating() {
            return rating;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public String getAddress() {
            return address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public List<PlacePhoto> getPhotos() {
            return photos;
        }
    }

    public static class Geometry {
        @SerializedName("location")
        Location location;

        @SerializedName("viewport")
        Viewport viewport;

        public void setLocation(Location location) {
            this.location = location;
        }

        public void setViewport(Viewport viewport) {
            this.viewport = viewport;
        }

        public Location getLocation() {
            return location;
        }

        public Viewport getViewport() {
            return viewport;
        }
    }

    public static class Location {
        @SerializedName("lat")
        double latitude;

        @SerializedName("lng")
        double longitude;

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public static class Viewport {
        @SerializedName("northeast")
        Location northeast;

        @SerializedName("southwest")
        Location southwest;

        public void setNortheast(Location northeast) {
            this.northeast = northeast;
        }

        public void setSouthwest(Location southwest) {
            this.southwest = southwest;
        }

        public Location getNortheast() {
            return northeast;
        }

        public Location getSouthwest() {
            return southwest;
        }
    }

    public static class PlacePhoto {
        @SerializedName("height")
        int height;

        @SerializedName("width")
        int width;

        @SerializedName("photo_reference")
        String photoReference;

        public void setHeight(int height) {
            this.height = height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setPhotoReference(String photoReference) {
            this.photoReference = photoReference;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public String getPhotoReference() {
            return photoReference;
        }

        public String getFullPhotoURLReference() {
            return PhotoUrlUtil.getPhotoUrl(photoReference);
        }
    }
}
