package info.alkor.whereareyou.senders;

import android.support.annotation.NonNull;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.model.LocationAction;

/**
 * Location request sender.
 * Created by Marlena on 2017-12-27.
 */
public class LocationRequestSender {

    private final WhereAreYouContext context;

    public LocationRequestSender(@NonNull WhereAreYouContext context) {
        this.context = context;
    }

    public void sendLocationRequest(@NonNull LocationAction action) {
        String content = context.getLocationRequestCommand();
        context.sendSms(action, content);
    }
}
