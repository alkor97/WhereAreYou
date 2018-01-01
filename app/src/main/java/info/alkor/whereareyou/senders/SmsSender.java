package info.alkor.whereareyou.senders;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;

import info.alkor.whereareyou.common.Requirements;
import info.alkor.whereareyou.model.LocationAction;

/**
 * SMS sending utility.
 * Created by Marlena on 2017-03-28.
 */
public class SmsSender {

    private final SmsManager manager = SmsManager.getDefault();

    /**
     * Send SMS with provided content to specified destination.
     *
     * @param destination phone number of message destination
     * @param content     content of message
     */
    @Requirements({"UC-SEND-SMS"})
    public void send(@NonNull String destination, @NonNull String content) {
        manager.sendTextMessage(destination, null, content, null, null);
    }

    public void send(@NonNull Context context, @NonNull LocationAction action, @NonNull String content) {
        manager.sendTextMessage(action.getPhoneNumber(),
                null,
                content,
                getDeliveryIntent(context, action.getActionId(), LocationAction.DeliveryStatus.SENT),
                getDeliveryIntent(context, action.getActionId(), LocationAction.DeliveryStatus.DELIVERED));
    }

    private PendingIntent getDeliveryIntent(Context context, long actionId, LocationAction.DeliveryStatus status) {
        Intent intent = new Intent(LocationBroadcasts.DELIVERY_STATUS_UPDATED);
        intent.putExtra(LocationBroadcasts.ACTION_ID, actionId);
        intent.putExtra(LocationBroadcasts.DELIVERY_STATUS, status.name());
        return getPendingIntent(context, intent);
    }

    private PendingIntent getPendingIntent(Context context, Intent intent) {
        return PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
