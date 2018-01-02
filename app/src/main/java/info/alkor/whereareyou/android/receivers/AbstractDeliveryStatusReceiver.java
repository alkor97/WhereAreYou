package info.alkor.whereareyou.android.receivers;

import android.content.Intent;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.senders.LocationBroadcasts;

/**
 * Abstract delivery status receiver.
 * Created by Marlena on 2018-01-02.
 */
public abstract class AbstractDeliveryStatusReceiver extends AbstractBroadcastReceiver {
    @Override
    protected void onReceive(WhereAreYouContext context, Intent intent) {
        long actionId = intent.getLongExtra(LocationBroadcasts.ACTION_ID, 0);
        LocationAction.DeliveryStatus status = LocationAction.DeliveryStatus.valueOf(intent.getStringExtra(LocationBroadcasts.DELIVERY_STATUS));
        onReceive(context, actionId, status);
    }

    public abstract void onReceive(WhereAreYouContext context, long actionId, LocationAction.DeliveryStatus deliveryStatus);
}
