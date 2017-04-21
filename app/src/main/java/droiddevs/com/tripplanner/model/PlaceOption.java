package droiddevs.com.tripplanner.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Jared12 on 4/13/17.
 */

public class PlaceOption {
    public enum PlaceOptionType {
        TYPE_CAFE("cafe"),
        TYPE_RESTAURANT("restaurant"),
        TYPE_DRINK("drinks"),
        TYPE_SAVED_PLACES("saved_places");

        private String typeString;

        PlaceOptionType(String typeString) {
            this.typeString = typeString;
        }

        public String typeSearchString() {
            return this.typeString;
        }

        public static PlaceOptionType fromString(String text) throws IllegalArgumentException {
            for (PlaceOptionType pt : PlaceOptionType.values()) {
                if (pt.typeString.equalsIgnoreCase(text)) {
                    return pt;
                }
            }
            throw new IllegalArgumentException("A PlaceOptionType representing the passed string does not exist");
        }
    }

    private Drawable mOptionImageDrawable;
    private String mOptionTitle;
    private PlaceOptionType mOptionType;

    public static PlaceOption newInstance(Drawable imageDrawable, PlaceOptionType placeType) {
        PlaceOption option = new PlaceOption();
        option.setOptionType(placeType);
        option.setOptionImageDrawable(imageDrawable);
        option.setOptionTitleByType(placeType);
        return option;
    }

    private void setOptionTitleByType(PlaceOptionType optionType) {
        switch (optionType) {
            case TYPE_DRINK:
                mOptionTitle = "Drinks";
                break;
            case TYPE_CAFE:
                mOptionTitle = "Cafes";
                break;
            case TYPE_RESTAURANT:
                mOptionTitle = "Restaurants";
                break;
            case TYPE_SAVED_PLACES:
                mOptionTitle = "Saved Places";
                break;
        }
    }

    public Drawable getOptionImageDrawable() {
        return mOptionImageDrawable;
    }

    public String getOptionTitle() {
        return mOptionTitle;
    }

    public PlaceOptionType getOptionType() {
        return mOptionType;
    }

    private void setOptionImageDrawable(Drawable optionImageDrawable) {
        this.mOptionImageDrawable = optionImageDrawable;
    }

    public void setOptionType(PlaceOptionType mOptionType) {
        this.mOptionType = mOptionType;
    }
}
