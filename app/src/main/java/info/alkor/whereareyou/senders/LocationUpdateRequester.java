package info.alkor.whereareyou.senders;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.util.Log;

import info.alkor.whereareyou.model.LocationAction;

/**
 * Location update sender.
 * Created by Marlena on 2018-01-04.
 */
public class LocationUpdateRequester {

    private final Context context;
    private final Class<?> locationUpdateReceiverClass;
    private LocationManager locationManager;

    public LocationUpdateRequester(@NonNull Context context, @NonNull Class<?>
            locationUpdateReceiverClass) {
        this.context = context;
        this.locationUpdateReceiverClass = locationUpdateReceiverClass;
    }

    public boolean requestSingleLocationUpdate(@NonNull String provider, @NonNull LocationAction
            action) {
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        if (locationManager != null) {
            Intent intent = new Intent(context, locationUpdateReceiverClass);
            intent.setAction(LocationBroadcasts.LOCATION_UPDATED);
            intent.putExtra(LocationBroadcasts.ACTION_ID, action.getActionId());

            try {
                locationManager.requestSingleUpdate(provider, getPendingIntent(intent));
            } catch (SecurityException e) {
                Log.e("permission", "No location permissions granted!");
                return false;
            }
            return true;
        } else {
            Log.e("service", "No location service available!");
        }
        return false;
    }

    private PendingIntent getPendingIntent(Intent intent) {
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
