package info.alkor.whereareyou;

import android.os.Handler;
import android.support.annotation.NonNull;

import info.alkor.whereareyou.android.ContactsHelper;
import info.alkor.whereareyou.location.LocationParser;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.persistence.ActionDataAccess;
import info.alkor.whereareyou.persistence.AppDatabase;
import info.alkor.whereareyou.persistence.UserDataAccess;
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
    LocationActionsSender getActionsSender();

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

    @NonNull
    UserDataAccess getUserDataAccess();

    @NonNull
    ActionDataAccess getActionDataAccess();

    @NonNull
    AppDatabase getDatabase();
}
