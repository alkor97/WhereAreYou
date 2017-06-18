package info.alkor.whereareyou.location.minimal;

import android.location.Location;
import android.support.annotation.NonNull;

import java.util.Date;

import info.alkor.whereareyou.location.LocationFormatter;
import info.alkor.whereareyou.location.LocationHelpers;

/**
 * Default location formatter.
 * Created by Marlena on 2017-06-07.
 */
class MinimalLocationFormatter implements LocationFormatter {

    private final LocationHelpers h = new LocationHelpers();

    @NonNull
    @Override
    public String format(@NonNull Location location) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                h.formatDate(new Date(location.getTime())),
                location.getProvider() != null ? location.getProvider() : "",
                h.formatCoordinate(location.getLatitude()),
                h.formatCoordinate(location.getLongitude()),
                location.hasAltitude() ? h.formatAltitude(location.getAltitude()) : "",
                location.hasAccuracy() ? h.formatAccuracy(location.getAccuracy()) : "",
                location.hasBearing() ? h.formatBearing(location.getBearing()) : "",
                location.hasSpeed() ? h.formatSpeed(location.getSpeed()) : ""
        );
    }
}
