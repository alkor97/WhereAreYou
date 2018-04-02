package info.alkor.whereareyou.receivers;

import android.location.Location;
import android.os.Handler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.android.receivers.AbstractLocationUpdateReceiver;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.persistence.ActionDataAccess;
import info.alkor.whereareyou.settings.LocationSettings;

/**
 * Location update receiver.
 * Created by Marlena on 2017-12-27.
 */
public class LocationUpdateReceiver extends AbstractLocationUpdateReceiver {

    private LocationSettings settings;
    private LocationQueryFlowManager flowManager;
    private Handler async;
    private ActionDataAccess actionDataAccess;

    @Override
    public void onReceive(WhereAreYouContext context, long actionId, Location location) {
        async = context.getDelayedHandler();
        settings = context.getApplicationSettings().getLocationSettings();
        flowManager = context.getLocationQueryFlowManager();
        actionDataAccess = context.getActionDataAccess();

        LocationAction action = flowManager.updateLocation(actionId, location);
        if (action != null) {
            scheduleUpdateSending(actionId);
        }
    }

    private void scheduleUpdateSending(long actionId) {
        async.postDelayed(makeRunnable(actionId), getMaxAwaitingTime());
    }

    private Runnable makeRunnable(final long actionId) {
        return new Runnable() {
            @Override
            public void run() {
                final List<LocationAction> actions = actionDataAccess.find(actionId).get();
                for (LocationAction action : actions) {
                    if (now() - action.getLocationTime() < getMaxAwaitingTime()) {
                        scheduleUpdateSending(actionId);
                    } else {
                        async.removeCallbacks(this);
                        flowManager.sendLocationResponse(action);
                    }
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
