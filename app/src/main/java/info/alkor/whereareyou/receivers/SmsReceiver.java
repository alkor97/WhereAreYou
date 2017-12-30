package info.alkor.whereareyou.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import info.alkor.whereareyou.R;
import info.alkor.whereareyou.WhereAreYou;
import info.alkor.whereareyou.common.Requirements;
import info.alkor.whereareyou.senders.LocationBroadcasts;
import info.alkor.whereareyou.location.minimal.MinimalLocationParser;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
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
        final String name = getDisplayName(context, phone);
        final String command = getLocationRequestCommand(context);
        if (message.getMessageBody().equals(command)) {
            flowManager.onIncomingLocationRequest(phone, name);

            LocationManager locationManager = getLocationManager(context);
            LocationSettings locationSettings = getLocationSettings(context);

            Intent intent = new Intent(LocationBroadcasts.LOCATION_UPDATED);
            intent.putExtra(LocationBroadcasts.PHONE, phone);
            intent.putExtra(LocationBroadcasts.NAME, name);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            for (String provider : locationSettings.getLocationProviders()) {
                locationManager.requestSingleUpdate(provider, pendingIntent);
            }
        } else {
            MinimalLocationParser parser = new MinimalLocationParser();
            Location location = parser.parse(message.getMessageBody());
            flowManager.onIncomingLocationResponse(phone, name, location);
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

    private String getDisplayName(Context context, String phone) {
        final Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));

        final Cursor contactLookup = context.getContentResolver().query(uri, new String[]{BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                return contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return null;
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
