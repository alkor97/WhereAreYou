package info.alkor.whereareyou.receivers;

import android.location.Location;
import android.os.Handler;

import java.util.concurrent.TimeUnit;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.android.receivers.AbstractLocationUpdateReceiver;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.settings.LocationSettings;

/**
 * Location update receiver.
 * Created by Marlena on 2017-12-27.
 */
public class LocationUpdateReceiver extends AbstractLocationUpdateReceiver {

    private LocationSettings settings;
    private LocationQueryFlowManager flowManager;
    private Handler async;

    @Override
    public void onReceive(WhereAreYouContext context, long actionId, Location location) {
        async = context.getDelayedHandler();
        settings = context.getApplicationSettings().getLocationSettings();
        flowManager = context.getLocationQueryFlowManager();

        LocationAction action = flowManager.updateLocation(actionId, location);
        if (action != null) {
            scheduleUpdateSending(action);
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
}
