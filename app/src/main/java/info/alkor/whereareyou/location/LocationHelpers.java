package info.alkor.whereareyou.location;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Set of location processing helpers.
 * Created by Marlena on 2017-06-07.
 */
public class LocationHelpers {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss",
            Locale.US);
    private static final TimeZone TZ_UTC = TimeZone.getTimeZone("UTC");
    private static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols
            (Locale.US);
    private static final NumberFormat COORDINATE_FORMAT = new DecimalFormat("#.######",
            DECIMAL_FORMAT_SYMBOLS);
    private static final NumberFormat ACCURACY_FORMAT = NumberFormat.getIntegerInstance(Locale.US);
    private static final NumberFormat ALTITUDE_FORMAT = NumberFormat.getIntegerInstance(Locale.US);
    private static final NumberFormat BEARING_FORMAT = NumberFormat.getIntegerInstance(Locale.US);
    private static final NumberFormat SPEED_FORMAT = new DecimalFormat("#.#",
            DECIMAL_FORMAT_SYMBOLS);

    static {
        DATE_FORMAT.setTimeZone(TZ_UTC);
    }

    public
    @NonNull
    String formatDate(@NonNull Date date) {
        return DATE_FORMAT.format(date);
    }

    public
    @NonNull
    Date parseDate(@NonNull String date) throws ParseException {
        return DATE_FORMAT.parse(date);
    }

    public
    @NonNull
    String formatCoordinate(double coordinate) {
        return COORDINATE_FORMAT.format(coordinate);
    }

    public double parseCoordinate(@NonNull String coordinate) throws ParseException {
        return COORDINATE_FORMAT.parse(coordinate).doubleValue();
    }

    public
    @NonNull
    String formatAccuracy(float accuracy) {
        return ACCURACY_FORMAT.format(accuracy);
    }

    public float parseAccuracy(@NonNull String accuracy) throws ParseException {
        return ACCURACY_FORMAT.parse(accuracy).floatValue();
    }

    public
    @NonNull
    String formatAltitude(double altitude) {
        return ALTITUDE_FORMAT.format(altitude);
    }

    public double parseAltitude(@NonNull String altitude) throws ParseException {
        return ALTITUDE_FORMAT.parse(altitude).doubleValue();
    }

    public
    @NonNull
    String formatBearing(float bearing) {
        return BEARING_FORMAT.format(bearing);
    }

    public float parseBearing(@NonNull String bearing) throws ParseException {
        return BEARING_FORMAT.parse(bearing).floatValue();
    }

    public
    @NonNull
    String formatSpeed(float speed) {
        return SPEED_FORMAT.format(speed);
    }

    public float parseSpeed(@NonNull String speed) throws ParseException {
        return SPEED_FORMAT.parse(speed).floatValue();
    }
}
