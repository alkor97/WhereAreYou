package info.alkor.whereareyou;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;

import org.acra.ACRA;
import org.acra.annotation.AcraDialog;
import org.acra.annotation.AcraMailSender;

import info.alkor.whereareyou.android.ContactsHelper;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationActionList;
import info.alkor.whereareyou.model.LocationActionManager;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.senders.LocationActionsSender;
import info.alkor.whereareyou.senders.LocationBroadcasts;
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

    @Override
    public ApplicationSettings getApplicationSettings() {
        return applicationSettings;
    }

    @Override
    public LocationActionList getModel() {
        return model;
    }

    @Override
    public LocationActionsSender getActionsSender() {
        return actionsSender;
    }

    @Override
    public LocationActionManager getModelManager() {
        return modelManager;
    }

    @Override
    public Handler getDelayedHandler() {
        return handler;
    }

    @Override
    public ContactsHelper getContactsHelper() {
        if (contactsHelper == null) {
            contactsHelper = new ContactsHelper(this);
        }
        return contactsHelper;
    }

    @Override
    public LocationQueryFlowManager getLocationQueryFlowManager() {
        if (flowManager == null) {
            flowManager = new LocationQueryFlowManager(this);
        }
        return flowManager;
    }

    @Override
    public String getLocationRequestCommand() {
        return getString(R.string.one_time_location_request);
    }

    @Override
    @SuppressLint("MissingPermission")
    public boolean requestSingleLocationUpdate(String provider, LocationAction action) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            Intent intent = new Intent(LocationBroadcasts.LOCATION_UPDATED);
            intent.putExtra(LocationBroadcasts.ACTION_ID, action.getActionId());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            locationManager.requestSingleUpdate(provider, pendingIntent);
            return true;
        }
        return false;
    }
}
