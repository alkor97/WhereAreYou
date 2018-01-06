package info.alkor.whereareyou.location.link;

import android.location.Location;
import android.support.annotation.NonNull;

import info.alkor.whereareyou.location.minimal.MinimalLocationFormatter;

/**
 * Link-based location formatter.
 * Created by Marlena on 2018-01-05.
 */
public class LocationLinkFormatter extends MinimalLocationFormatter {

    private final String smsLinkPrefix;

    public LocationLinkFormatter(@NonNull String smsLinkPrefix) {
        this.smsLinkPrefix = smsLinkPrefix;
    }

    @Override
    @NonNull
    public String format(@NonNull Location location) {
        return String.format("%s%s",
                smsLinkPrefix,
                super.format(location));
    }
}
