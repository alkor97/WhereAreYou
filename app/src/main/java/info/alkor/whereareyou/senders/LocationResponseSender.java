package info.alkor.whereareyou.senders;

import info.alkor.whereareyou.location.minimal.MinimalLocationFormatter;
import info.alkor.whereareyou.model.LocationAction;

/**
 * Created by Marlena on 2017-12-27.
 */

public class LocationResponseSender {

    private final SmsSender sender = new SmsSender();

    public void sendLocationResponse(LocationAction action) {
        //String response = String.format("http://loc.alkor.info/?q=%s", responseFormatter.format(location));
        MinimalLocationFormatter responseFormatter = new MinimalLocationFormatter();
        sender.send(action.getSide().getPhone(), responseFormatter.format(action.getLocation()));
    }
}
