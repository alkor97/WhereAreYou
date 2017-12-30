package info.alkor.whereareyou.receivers;

import android.content.Intent;

import info.alkor.whereareyou.senders.LocationBroadcasts;
import info.alkor.whereareyou.senders.LocationActionsSender;
import info.alkor.whereareyou.ui.LocationActionAdapter;

/**
 * Created by Marlena on 2017-12-23.
 */

public class LocationActionsReceiver extends AbstractLocationReceiver {

    private final LocationActionAdapter adapter;

    public LocationActionsReceiver(final LocationActionsSender helper, LocationActionAdapter adapter) {
        super(helper);
        this.adapter = adapter;

        registerReceiver(LocationBroadcasts.LOCATION_ACTION_ADDED, new IntentReceiver() {
            @Override
            public void onReceive(Intent intent) {
                addItem(helper.getPosition(intent));
            }
        });

        registerReceiver(LocationBroadcasts.LOCATION_ACTION_CHANGED, new IntentReceiver() {
            @Override
            public void onReceive(Intent intent) {
                changeItem(helper.getPosition(intent));
            }
        });
    }

    private void addItem(int position) {
        adapter.notifyItemInserted(position);
    }

    private void changeItem(int position) {
        adapter.notifyItemChanged(position);
    }
}
