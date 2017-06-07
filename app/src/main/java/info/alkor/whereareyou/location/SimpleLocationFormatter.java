package info.alkor.whereareyou.location;

import android.location.Location;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Response formatter.
 * Created by Marlena on 2017-03-28.
 */
public class SimpleLocationFormatter implements LocationFormatter {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ",
            Locale.US);
    private static final NumberFormat ACCURACY_FORMAT = NumberFormat.getIntegerInstance(Locale.US);
    private static final NumberFormat ALTITUDE_FORMAT = NumberFormat.getIntegerInstance(Locale.US);
    private static final NumberFormat BEARING_FORMAT = NumberFormat.getIntegerInstance(Locale.US);
    private static final NumberFormat SPEED_FORMAT = new DecimalFormat("#.#");

    @NonNull
    public String format(@NonNull Location location) {
        return String.format("%s (%s %s%s)", getGoogleMapsLink(location), getTime(location),
                getProvider(location), getOptionalsString(location));
    }

    protected String getGoogleMapsLink(Location location) {
        return String.format(Locale.US, "https://www.google.com/maps/place/%.8f,%.8f", location
                .getLatitude(), location.getLongitude());
    }

    private String getTime(Location location) {
        Date date = new Date(location.getTime());
        return DATE_FORMAT.format(date);
    }

    private String getProvider(Location location) {
        return location.getProvider();
    }

    private String getOptionalsString(Location location) {
        StringBuilder sb = new StringBuilder();
        if (location.hasAccuracy()) {
            sb.append(" ~").append(ACCURACY_FORMAT.format(location.getAccuracy())).append("m");
        }
        if (location.hasAltitude()) {
            sb.append(" ^").append(ALTITUDE_FORMAT.format(location.getAltitude())).append("m");
        }
        if (location.hasBearing()) {
            sb.append(" ").append(BEARING_FORMAT.format(location.getBearing())).append("deg");
        }
        if (location.hasSpeed()) {
            sb.append(" ").append(SPEED_FORMAT.format(location.getSpeed())).append("m/s");
        }
        return sb.toString();
    }
}
