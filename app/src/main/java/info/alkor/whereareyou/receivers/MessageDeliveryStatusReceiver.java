package info.alkor.whereareyou.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.senders.LocationBroadcasts;

/**
 * Created by Marlena on 2017-12-31.
 */

public class MessageDeliveryStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long actionId = intent.getLongExtra(LocationBroadcasts.ACTION_ID, 0);
        LocationAction.DeliveryStatus status = LocationAction.DeliveryStatus.valueOf(intent.getStringExtra(LocationBroadcasts.DELIVERY_STATUS));

        LocationQueryFlowManager flowManager = new LocationQueryFlowManager(context);
        flowManager.updateDeliveryStatus(actionId, status);
    }
}
