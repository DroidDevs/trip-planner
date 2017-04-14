package droiddevs.com.tripplanner.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Jared12 on 4/13/17.
 */

public class DestinationOption {
    private Drawable mOptionImageDrawable;
    private String mOptionTitle;

    public static DestinationOption newInstance(Drawable imageDrawable, String optionTitle) {
        DestinationOption option = new DestinationOption();
        option.setOptionImageDrawable(imageDrawable);
        option.setOptionTitle(optionTitle);
        return option;
    }

    public Drawable getOptionImageDrawable() {
        return mOptionImageDrawable;
    }

    public String getOptionTitle() {
        return mOptionTitle;
    }

    private void setOptionImageDrawable(Drawable optionImageDrawable) {
        this.mOptionImageDrawable = optionImageDrawable;
    }

    private void setOptionTitle(String mOptionTitle) {
        this.mOptionTitle = mOptionTitle;
    }
}
