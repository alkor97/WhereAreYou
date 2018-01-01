package info.alkor.whereareyou.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;

import java.util.concurrent.TimeUnit;

import info.alkor.whereareyou.WhereAreYou;
import info.alkor.whereareyou.senders.LocationBroadcasts;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.settings.LocationSettings;

/**
 * Created by Marlena on 2017-12-27.
 */

public class LocationUpdateReceiver extends BroadcastReceiver {

    private LocationSettings settings;
    private LocationQueryFlowManager flowManager;
    private Handler async;

    @Override
    public void onReceive(Context context, Intent intent) {
        async = getDelayedHandler(context);
        settings = getLocationSettings(context);
        flowManager = new LocationQueryFlowManager(context);

        Location location = intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if (location != null) {
            long actionId = intent.getLongExtra(LocationBroadcasts.ACTION_ID, 0);

            LocationAction action = flowManager.updateLocation(actionId, location);
            if (action != null) {
                scheduleUpdateSending(action);
            }
        }
    }

    private void scheduleUpdateSending(LocationAction action) {
        async.postDelayed(makeRunnable(action), getMaxAwaitingTime());
    }

    private Runnable makeRunnable(final LocationAction action) {
        return new Runnable() {
            @Override
            public void run() {
                if (now() - action.getLocationTime() < getMaxAwaitingTime()) {
                    scheduleUpdateSending(action);
                } else {
                    async.removeCallbacks(this);
                    flowManager.sendLocationResponse(action);
                }
            }
        };
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private long getMaxAwaitingTime() {
        return settings.getMaxAwaitTimeForBetterLocationAccuracy(TimeUnit.MILLISECONDS);
    }

    private Handler getDelayedHandler(Context context) {
        return getApplication(context).getDelayedHandler();
    }

    private LocationSettings getLocationSettings(Context context) {
        return getApplication(context).getApplicationSettings().getLocationSettings();
    }

    private WhereAreYou getApplication(Context context) {
        return (WhereAreYou) context.getApplicationContext();
    }
}
