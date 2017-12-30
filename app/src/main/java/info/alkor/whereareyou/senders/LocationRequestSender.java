package info.alkor.whereareyou.senders;

import android.content.Context;

import info.alkor.whereareyou.R;
import info.alkor.whereareyou.model.LocationActionSide;

/**
 * Created by Marlena on 2017-12-27.
 */

public class LocationRequestSender {

    private final SmsSender sender = new SmsSender();
    private final Context context;

    public LocationRequestSender(Context context) {
        this.context = context;
    }

    public void sendLocationRequest(LocationActionSide provider) {
        String content = context.getString(R.string.one_time_location_request);
        sender.send(provider.getPhone(), content);
    }
}
