package info.alkor.whereareyou;

import android.app.Application;
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
import info.alkor.whereareyou.model.LocationActionList;
import info.alkor.whereareyou.model.LocationActionManager;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.senders.LocationActionsSender;
import info.alkor.whereareyou.senders.LocationUpdateRequester;
import info.alkor.whereareyou.senders.SmsSender;
import info.alkor.whereareyou.settings.ApplicationSettings;
import info.alkor.whereareyou.settings.LocationSettings;
import info.alkor.whereareyou.settings.UserManager;

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
    private LocationActionList model;
    private LocationActionsSender actionsSender;
    private LocationActionManager modelManager;
    private ContactsHelper contactsHelper;
    private LocationQueryFlowManager flowManager;
    private LocationParser locationParser;
    private SmsSender smsSender;
    private LocationUpdateRequester locationUpdateRequester;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationSettings = new ApplicationSettings(new LocationSettings(), new UserManager());
        model = new LocationActionList();

        actionsSender = new LocationActionsSender(this);
        modelManager = new LocationActionManager(model, actionsSender);
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
    public LocationActionList getModel() {
        return model;
    }

    @NonNull
    @Override
    public LocationActionsSender getActionsSender() {
        return actionsSender;
    }

    @NonNull
    @Override
    public LocationActionManager getModelManager() {
        return modelManager;
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
    public boolean requestSingleLocationUpdate(@NonNull String provider, @NonNull LocationAction action) {
        if (locationUpdateRequester == null) {
            locationUpdateRequester = new LocationUpdateRequester(this);
        }
        return locationUpdateRequester.requestSingleLocationUpdate(provider, action);
    }

    @Override
    public void sendSms(@NonNull LocationAction action, @NonNull String content) {
        if (smsSender == null) {
            smsSender = new SmsSender();
        }
        smsSender.send(this, action, content);
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
}
