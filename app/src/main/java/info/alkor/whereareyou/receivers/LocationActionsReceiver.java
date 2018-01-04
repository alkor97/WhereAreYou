package info.alkor.whereareyou.receivers;

import android.content.Intent;

import info.alkor.whereareyou.senders.LocationActionsSender;
import info.alkor.whereareyou.senders.LocationBroadcasts;
import info.alkor.whereareyou.ui.LocationActionAdapter;

/**
 * Location action operation receiver updating UI control.
 * Created by Marlena on 2017-12-23.
 */
public class LocationActionsReceiver extends AbstractLocationReceiver {

    public LocationActionsReceiver(final LocationActionsSender helper, final LocationActionAdapter adapter) {
        super(helper);

        registerReceiver(LocationBroadcasts.LOCATION_ACTION_ADDED, new IntentReceiver() {
            @Override
            public void onReceive(Intent intent) {
                adapter.notifyItemInserted(helper.getPosition(intent));
            }
        });

        registerReceiver(LocationBroadcasts.LOCATION_ACTION_CHANGED, new IntentReceiver() {
            @Override
            public void onReceive(Intent intent) {
                adapter.notifyItemChanged(helper.getPosition(intent));
            }
        });

        registerReceiver(LocationBroadcasts.LOCATION_ACTION_REMOVED, new IntentReceiver() {
            @Override
            public void onReceive(Intent intent) {
                adapter.notifyItemRemoved(helper.getPosition(intent));
            }
        });
    }
}
