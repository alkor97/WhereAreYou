package info.alkor.whereareyou;

import android.os.Handler;
import android.support.annotation.NonNull;

import info.alkor.whereareyou.android.ContactsHelper;
import info.alkor.whereareyou.location.LocationParser;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationActionManager;
import info.alkor.whereareyou.model.LocationActions;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.senders.LocationActionsSender;
import info.alkor.whereareyou.settings.ApplicationSettings;

/**
 * Main application context interface.
 * Created by Marlena on 2018-01-01.
 */
public interface WhereAreYouContext {

    @NonNull
    ApplicationSettings getApplicationSettings();

    @NonNull
    LocationActions getModel();

    @NonNull
    LocationActionsSender getActionsSender();

    @NonNull
    LocationActionManager getModelManager();

    @NonNull
    Handler getDelayedHandler();

    @NonNull
    ContactsHelper getContactsHelper();

    @NonNull
    LocationQueryFlowManager getLocationQueryFlowManager();

    @NonNull
    String getLocationRequestCommand();

    boolean requestSingleLocationUpdate(@NonNull String provider, @NonNull LocationAction action);

    void sendSms(@NonNull LocationAction action, @NonNull String content);

    @NonNull
    LocationParser getLocationParser();

    @NonNull
    String getSmsLinkPrefix();
}
