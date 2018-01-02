package info.alkor.whereareyou.android.receivers;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.senders.LocationBroadcasts;

/**
 * Abstract location update receiver.
 * Created by Marlena on 2018-01-02.
 */
public abstract class AbstractLocationUpdateReceiver extends AbstractBroadcastReceiver {
    @Override
    protected void onReceive(WhereAreYouContext context, Intent intent) {
        Location location = intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if (location != null) {
            long actionId = intent.getLongExtra(LocationBroadcasts.ACTION_ID, 0);
            onReceive(context, actionId, location);
        }
    }

    public abstract void onReceive(WhereAreYouContext context, long actionId, Location location);
}
