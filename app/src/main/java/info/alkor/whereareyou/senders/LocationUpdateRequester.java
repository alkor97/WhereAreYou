package info.alkor.whereareyou.senders;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import info.alkor.whereareyou.model.LocationAction;

/**
 * Location update sender.
 * Created by Marlena on 2018-01-04.
 */
public class LocationUpdateRequester {

    private final Context context;
    private LocationManager locationManager;

    public LocationUpdateRequester(@NonNull Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    public boolean requestSingleLocationUpdate(@NonNull String provider, @NonNull LocationAction action) {
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        if (locationManager != null) {
            Intent intent = new Intent(LocationBroadcasts.LOCATION_UPDATED);
            intent.putExtra(LocationBroadcasts.ACTION_ID, action.getActionId());

            locationManager.requestSingleUpdate(provider, getPendingIntent(intent));
            return true;
        }
        return false;
    }

    private PendingIntent getPendingIntent(Intent intent) {
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
