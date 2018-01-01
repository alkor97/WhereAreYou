package info.alkor.whereareyou;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import org.acra.ACRA;
import org.acra.annotation.AcraDialog;
import org.acra.annotation.AcraMailSender;

import info.alkor.whereareyou.android.ContactsHelper;
import info.alkor.whereareyou.android.SmsHelper;
import info.alkor.whereareyou.model.LocationActionList;
import info.alkor.whereareyou.model.LocationActionManager;
import info.alkor.whereareyou.senders.LocationActionsSender;
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
public class WhereAreYou extends Application {

    private final Handler handler = new Handler();
    private ApplicationSettings applicationSettings;
    private LocationActionList model;
    private LocationActionsSender actionsSender;
    private LocationActionManager modelManager;
    private ContactsHelper contactsHelper;
    private SmsHelper smsHelper;

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

    public ApplicationSettings getApplicationSettings() {
        return applicationSettings;
    }

    public LocationActionList getModel() {
        return model;
    }

    public LocationActionsSender getActionsSender() {
        return actionsSender;
    }

    public LocationActionManager getModelManager() {
        return modelManager;
    }

    public Handler getDelayedHandler() {
        return handler;
    }

    public ContactsHelper getContactsHelper() {
        if (contactsHelper == null) {
            contactsHelper = new ContactsHelper(this);
        }
        return contactsHelper;
    }

    public SmsHelper getSmsHelper() {
        if (smsHelper == null) {
            smsHelper = new SmsHelper();
        }
        return smsHelper;
    }
}
