package info.alkor.whereareyou;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import org.acra.ACRA;
import org.acra.annotation.AcraDialog;
import org.acra.annotation.AcraMailSender;

import info.alkor.whereareyou.android.ContactsHelper;
import info.alkor.whereareyou.location.LocationParser;
import info.alkor.whereareyou.location.link.LocationLinkParser;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.persistence.ActionDataAccess;
import info.alkor.whereareyou.persistence.AppDatabase;
import info.alkor.whereareyou.persistence.UserDataAccess;
import info.alkor.whereareyou.senders.LocationUpdateRequester;
import info.alkor.whereareyou.senders.SmsSender;
import info.alkor.whereareyou.settings.ApplicationSettings;
import info.alkor.whereareyou.settings.LocationSettings;

/**
 * Main application context.
 * Created by Marlena on 2017-12-25.
 */
@AcraMailSender(mailTo = "wjewjurka@gmail.com")
@AcraDialog(
        resText = R.string.app_crash_report_query,
        resPositiveButtonText = R.string.app_yes,
        resNegativeButtonText = R.string.app_no
)
public class WhereAreYou extends Application implements WhereAreYouContext {

    private final Handler handler = new Handler();
    private ApplicationSettings applicationSettings;
    private ContactsHelper contactsHelper;
    private LocationQueryFlowManager flowManager;
    private LocationParser locationParser;
    private SmsSender smsSender;
    private LocationUpdateRequester locationUpdateRequester;
    private AppDatabase database;
    private UserDataAccess userDataAccess;
    private ActionDataAccess actionDataAccess;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationSettings = new ApplicationSettings(new LocationSettings());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }

    @NonNull
    @Override
    public ApplicationSettings getApplicationSettings() {
        return applicationSettings;
    }

    @NonNull
    @Override
    public Handler getDelayedHandler() {
        return handler;
    }

    @NonNull
    @Override
    public ContactsHelper getContactsHelper() {
        if (contactsHelper == null) {
            contactsHelper = new ContactsHelper(this);
        }
        return contactsHelper;
    }

    @NonNull
    @Override
    public LocationQueryFlowManager getLocationQueryFlowManager() {
        if (flowManager == null) {
            flowManager = new LocationQueryFlowManager(this);
        }
        return flowManager;
    }

    @NonNull
    @Override
    public String getLocationRequestCommand() {
        return getString(R.string.one_time_location_request);
    }

    @Override
    public boolean requestSingleLocationUpdate(@NonNull String provider, @NonNull LocationAction
            action) {
        if (locationUpdateRequester == null) {
            locationUpdateRequester = new LocationUpdateRequester(this, info.alkor.whereareyou
                    .receivers.LocationUpdateReceiver.class);
        }
        return locationUpdateRequester.requestSingleLocationUpdate(provider, action);
    }

    @Override
    public void sendSms(@NonNull LocationAction action, @NonNull String content) {
        if (smsSender == null) {
            smsSender = new SmsSender(this, info.alkor.whereareyou.receivers
                    .MessageDeliveryStatusReceiver.class);
        }
        smsSender.send(action, content);
    }

    @NonNull
    @Override
    public LocationParser getLocationParser() {
        if (locationParser == null) {
            locationParser = new LocationLinkParser(getSmsLinkPrefix());
        }
        return locationParser;
    }

    @NonNull
    public String getSmsLinkPrefix() {
        return getString(R.string.smsLinkPrefix);
    }

    @NonNull
    @Override
    public AppDatabase getDatabase() {
        if (database == null) {
            database = Room.databaseBuilder(this, AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    @NonNull
    @Override
    public UserDataAccess getUserDataAccess() {
        if (userDataAccess == null) {
            userDataAccess = new UserDataAccess(this);
        }
        return userDataAccess;
    }

    @NonNull
    @Override
    public ActionDataAccess getActionDataAccess() {
        if (actionDataAccess == null) {
            actionDataAccess = new ActionDataAccess(this);
        }
        return actionDataAccess;
    }
}
