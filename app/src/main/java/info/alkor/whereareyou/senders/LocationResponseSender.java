package info.alkor.whereareyou.senders;

import android.content.Context;

import info.alkor.whereareyou.location.minimal.MinimalLocationFormatter;
import info.alkor.whereareyou.model.LocationAction;

/**
 * Created by Marlena on 2017-12-27.
 */

public class LocationResponseSender {

    private final Context context;
    private final SmsSender sender = new SmsSender();

    public LocationResponseSender(Context context) {
        this.context = context;
    }

    public void sendLocationResponse(LocationAction action) {
        MinimalLocationFormatter responseFormatter = new MinimalLocationFormatter();
        sender.send(context, action, responseFormatter.format(action.getLocation()));
    }
}
