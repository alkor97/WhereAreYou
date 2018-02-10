package info.alkor.whereareyou.receivers;

import android.location.Location;
import android.util.Log;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.android.receivers.AbstractSmsReceiver;
import info.alkor.whereareyou.location.LocationParser;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.settings.LocationSettings;

/**
 * SMS receiver.
 * Created by Marlena on 2017-03-24.
 */
public class SmsReceiver extends AbstractSmsReceiver {

    @Override
    public void onReceive(WhereAreYouContext context, String phone, String name, String
            messageBody) {
        LocationQueryFlowManager flowManager = context.getLocationQueryFlowManager();

        final String command = context.getLocationRequestCommand();
        if (messageBody.equals(command)) {
            LocationAction action = flowManager.onIncomingLocationRequest(phone, name);
            // null action means that request of same side is already being processed
            if (action != null) {
                LocationSettings locationSettings = context.getApplicationSettings()
                        .getLocationSettings();

                for (String provider : locationSettings.getLocationProviders()) {
                    if (!context.requestSingleLocationUpdate(provider, action)) {
                        Log.e("location", String.format("unable to request location update for " +
                                "provider %s", provider));
                    }
                }
            }
        } else {
            LocationParser parser = context.getLocationParser();
            try {
                Location location = parser.parse(messageBody);
                flowManager.onIncomingLocationResponse(phone, name, location);
            } catch (LocationParser.ParsingException e) {
                Log.e("parsing", e.getLocalizedMessage());
                // no match in SMS content, ignore
            }
        }
    }
}
