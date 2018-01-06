package info.alkor.whereareyou.senders;

import android.support.annotation.NonNull;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.location.link.LocationLinkFormatter;
import info.alkor.whereareyou.model.LocationAction;

/**
 * Location response sender.
 * Created by Marlena on 2017-12-27.
 */
public class LocationResponseSender {

    private final WhereAreYouContext context;

    public LocationResponseSender(@NonNull WhereAreYouContext context) {
        this.context = context;
    }

    public void sendLocationResponse(@NonNull LocationAction action) {
        LocationLinkFormatter responseFormatter = new LocationLinkFormatter(context.getSmsLinkPrefix());
        context.sendSms(action, responseFormatter.format(action.getLocation()));
    }
}
