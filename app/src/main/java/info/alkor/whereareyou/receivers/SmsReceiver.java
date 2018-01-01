package info.alkor.whereareyou.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import info.alkor.whereareyou.R;
import info.alkor.whereareyou.WhereAreYou;
import info.alkor.whereareyou.android.ContactsHelper;
import info.alkor.whereareyou.common.Requirements;
import info.alkor.whereareyou.location.LocationParser;
import info.alkor.whereareyou.location.minimal.MinimalLocationParser;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.senders.LocationBroadcasts;
import info.alkor.whereareyou.settings.LocationSettings;

/**
 * SMS receiver.
 * Created by Marlena on 2017-03-24.
 */
public class SmsReceiver extends BroadcastReceiver {

    @Requirements({"UC-RECEIVE-SMS"})
    @Override
    public void onReceive(final Context context, Intent intent) {
        final SmsMessage message = getMessage(intent);
        if (message != null) {
            dispatchMessage(context, message);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void dispatchMessage(Context context, SmsMessage message) {
        LocationQueryFlowManager flowManager = new LocationQueryFlowManager(context);

        final String phone = message.getOriginatingAddress();
        final String name = getContactsHelper(context).getDisplayName(phone);
        final String command = getLocationRequestCommand(context);
        if (message.getMessageBody().equals(command)) {
            LocationAction action = flowManager.onIncomingLocationRequest(phone, name);
            // null action means that request of same side is already being processed
            if (action != null) {
                LocationManager locationManager = getLocationManager(context);
                LocationSettings locationSettings = getLocationSettings(context);

                Intent intent = new Intent(LocationBroadcasts.LOCATION_UPDATED);
                intent.putExtra(LocationBroadcasts.ACTION_ID, action.getActionId());

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                for (String provider : locationSettings.getLocationProviders()) {
                    locationManager.requestSingleUpdate(provider, pendingIntent);
                }
            }
        } else {
            MinimalLocationParser parser = new MinimalLocationParser();
            try {
                Location location = parser.parse(message.getMessageBody());
                flowManager.onIncomingLocationResponse(phone, name, location);
            } catch (LocationParser.ParsingException e) {
                Log.e("parsing", e.getLocalizedMessage());
                // no match in SMS content, ignore
            }
        }
    }

    private String getLocationRequestCommand(Context context) {
        return context.getString(R.string.one_time_location_request);
    }

    private SmsMessage getMessage(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            return messages[0];
        } else {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                final Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null && pdus.length > 0) {
                    return SmsMessage.createFromPdu((byte[]) pdus[0]);
                }
            }
        }
        return null;
    }

    private ContactsHelper getContactsHelper(Context context) {
        return getApplication(context).getContactsHelper();
    }

    private LocationManager getLocationManager(Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    private LocationSettings getLocationSettings(Context context) {
        return getApplication(context).getApplicationSettings().getLocationSettings();
    }

    private WhereAreYou getApplication(Context context) {
        return (WhereAreYou) context.getApplicationContext();
    }
}
