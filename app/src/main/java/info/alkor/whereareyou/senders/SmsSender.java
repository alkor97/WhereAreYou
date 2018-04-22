package info.alkor.whereareyou.senders;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.util.Log;

import info.alkor.whereareyou.model.LocationAction;

/**
 * SMS sending utility.
 * Created by Marlena on 2017-03-28.
 */
public class SmsSender {

    private final SmsManager manager = SmsManager.getDefault();
    private final Context context;
    private final Class<?> messageDeliveryReceiverClass;

    public SmsSender(@NonNull Context context, @NonNull Class<?> messageDeliveryReceiverClass) {
        this.context = context;
        this.messageDeliveryReceiverClass = messageDeliveryReceiverClass;
    }

    public void send(@NonNull LocationAction action, @NonNull String
            content) {
        try {
            manager.sendTextMessage(action.getPhoneNumber(),
                    null,
                    content,
                    getDeliveryIntent(context, action.getActionId(), LocationAction.DeliveryStatus
                            .SENT),
                    getDeliveryIntent(context, action.getActionId(), LocationAction.DeliveryStatus
                            .DELIVERED));
        } catch (SecurityException e) {
            Log.e("permission", "No SMS sending permissions granted!");
        }
    }

    private PendingIntent getDeliveryIntent(Context context, long actionId, LocationAction
            .DeliveryStatus status) {
        Intent intent = new Intent(context, messageDeliveryReceiverClass);
        intent.setAction(LocationBroadcasts.DELIVERY_STATUS_UPDATED);
        intent.putExtra(LocationBroadcasts.ACTION_ID, actionId);
        intent.putExtra(LocationBroadcasts.DELIVERY_STATUS, status.name());
        return getPendingIntent(context, intent);
    }

    private PendingIntent getPendingIntent(Context context, Intent intent) {
        return PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
