package info.alkor.whereareyou.senders;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Marlena on 2017-12-20.
 */

public class LocationActionsSender {

    private final Context context;
    private LocalBroadcastManager broadcastManager;

    public LocationActionsSender(Context context) {
        this.context = context;
    }

    private LocalBroadcastManager getBroadcastManager() {
        if (broadcastManager == null) {
            broadcastManager = LocalBroadcastManager.getInstance(context);
        }
        return broadcastManager;
    }

    public void notifyActionAdded(int position) {
        final Intent intent = new Intent();
        intent.setAction(LocationBroadcasts.LOCATION_ACTION_ADDED);
        setPosition(intent, position);
        getBroadcastManager().sendBroadcastSync(intent);
    }

    public void notifyActionChanged(int position) {
        final Intent intent = new Intent();
        intent.setAction(LocationBroadcasts.LOCATION_ACTION_CHANGED);
        setPosition(intent, position);
        getBroadcastManager().sendBroadcastSync(intent);
    }

    public void setPosition(Intent intent, int position) {
        intent.putExtra(LocationBroadcasts.POSITION, position);
    }

    public int getPosition(Intent intent) {
        return intent.getIntExtra(LocationBroadcasts.POSITION, 0);
    }
}
