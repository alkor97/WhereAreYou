package info.alkor.whereareyou.ui;

import android.content.Context;
import android.location.Location;
import android.text.format.DateUtils;
import android.view.View;

import info.alkor.whereareyou.R;
import info.alkor.whereareyou.common.TextHelper;
import info.alkor.whereareyou.location.minimal.MinimalLocationFormatter;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationActionSide;

/**
 * Location action view model.
 * Created by Marlena on 2018-01-28.
 */
public class LocationActionViewModel {

    private static final MinimalLocationFormatter FORMATTER = new MinimalLocationFormatter();
    private static final String[] DIRECTIONS = {"↑N", "↗NE", "→E", "↘SE", "↓S", "↙SW", "←W", "↖NW"};
    private static final TextHelper TEXT_HELPER = new TextHelper();

    private final Context context;
    private final LocationAction action;

    LocationActionViewModel(Context context, LocationAction action) {
        this.context = context;
        this.action = action;
    }

    public Location getLocation() {
        return action.getLocation();
    }

    public String getTime() {
        final long time = action.getLocation() != null
                ? action.getLocation().getTime()
                : System.currentTimeMillis();
        return DateUtils.formatDateTime(context, time, DateUtils.FORMAT_SHOW_DATE | DateUtils
                .FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_WEEKDAY);
    }

    public String getDeliveryStatus() {
        if (action.getDeliveryStatus() == LocationAction.DeliveryStatus.SENT) {
            return "➜";
        } else if (action.getDeliveryStatus() == LocationAction.DeliveryStatus.DELIVERED) {
            return "✓";
        }
        return "…";
    }

    public String getSideType() {
        if (action.getSide().getType() == LocationActionSide.Type.REQUESTER) {
            return "➘";
        }
        return "➚";
    }

    private String getFormattedPhone() {
        return TEXT_HELPER.formatPhone(action.getPhoneNumber());
    }

    private String getDecoratedPhone() {
        return String.format("✆  %s", getFormattedPhone());
    }

    public String getSide() {
        if (action.getDisplayName() != null) {
            return action.getDisplayName();
        } else {
            return getFormattedPhone();
        }
    }

    public String getPhone() {
        return action.getDisplayName() != null ? getDecoratedPhone() : "";
    }

    public int getPhoneVisibility() {
        return toVisibility(action.getDisplayName() != null);
    }

    private int toVisibility(boolean has) {
        return has ? View.VISIBLE : View.GONE;
    }

    private boolean hasCoordinates() {
        return action.getLocation() != null;
    }

    public int getCoordinatesVisibility() {
        return toVisibility(hasCoordinates());
    }

    public String getCoordinates() {
        return hasCoordinates() ? context.getString(R.string.row_main_position,
                action.getLocation().getLatitude(),
                action.getLocation().getLongitude(),
                action.getLocation().getProvider())
                : "";
    }

    private boolean hasAltitude() {
        return action.getLocation() != null && action.getLocation().hasAltitude();
    }

    public int getAltitudeVisibility() {
        return toVisibility(hasAltitude());
    }

    public String getAltitude() {
        return hasAltitude() ? context.getString(R.string.row_main_altitude,
                action.getLocation().getAltitude(),
                "m") : "";
    }

    private boolean hasAccuracy() {
        return action.getLocation() != null && action.getLocation().hasAccuracy();
    }

    public int getAccuracyVisibility() {
        return toVisibility(hasAccuracy());
    }

    public String getAccuracy() {
        return hasAccuracy() ? context.getString(R.string.row_main_accuracy,
                action.getLocation().getAccuracy(),
                "m") : "";
    }

    private String getDirection(float bearing) {
        float normalized = (bearing - 22.5f) / 45;
        return DIRECTIONS[(int) Math.ceil(normalized) % DIRECTIONS.length];
    }

    private boolean hasBearing() {
        return action.getLocation() != null && action.getLocation().hasBearing();
    }

    public int getBearingVisibility() {
        return toVisibility(hasBearing());
    }

    public String getBearing() {
        return hasBearing() ? context.getString(R.string.row_main_azimuth,
                action.getLocation().getBearing(),
                getDirection(action.getLocation().getBearing())) : "";
    }

    private boolean hasSpeed() {
        return action.getLocation() != null && action.getLocation().hasSpeed();
    }

    public int getSpeedVisibility() {
        return toVisibility(hasSpeed());
    }

    public String getSpeed() {
        return hasSpeed() ? context.getString(R.string.row_main_speed,
                action.getLocation().getSpeed() * 3.6,
                "km/h") : "";
    }
}
