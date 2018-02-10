package info.alkor.whereareyou.receivers;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.android.receivers.AbstractDeliveryStatusReceiver;
import info.alkor.whereareyou.model.LocationAction;

/**
 * SMS message delivery status receiver.
 * Created by Marlena on 2017-12-31.
 */
public class MessageDeliveryStatusReceiver extends AbstractDeliveryStatusReceiver {
    @Override
    public void onReceive(WhereAreYouContext context, long actionId, LocationAction
            .DeliveryStatus deliveryStatus) {
        context.getLocationQueryFlowManager().updateDeliveryStatus(actionId, deliveryStatus);
    }
}
