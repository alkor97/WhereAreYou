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

    private final Handler async = new Handler();
    private LocationSettings settings;
    private LocationQueryFlowManager flowManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        settings = getLocationSettings(context);
        flowManager = new LocationQueryFlowManager(context);

        Location location = intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if (location != null) {
            String phone = intent.getStringExtra(LocationBroadcasts.PHONE);
            String name = intent.getStringExtra(LocationBroadcasts.NAME);

            LocationAction action = flowManager.updateLocation(phone, name, location);
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

    private LocationSettings getLocationSettings(Context context) {
        return getApplication(context).getApplicationSettings().getLocationSettings();
    }

    private WhereAreYou getApplication(Context context) {
        return (WhereAreYou) context.getApplicationContext();
    }
}
