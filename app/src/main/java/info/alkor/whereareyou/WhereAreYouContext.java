package info.alkor.whereareyou;

import android.os.Handler;

import info.alkor.whereareyou.android.ContactsHelper;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationActionList;
import info.alkor.whereareyou.model.LocationActionManager;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.senders.LocationActionsSender;
import info.alkor.whereareyou.settings.ApplicationSettings;

/**
 * Main application context interface.
 * Created by Marlena on 2018-01-01.
 */
public interface WhereAreYouContext {

    ApplicationSettings getApplicationSettings();

    LocationActionList getModel();

    LocationActionsSender getActionsSender();

    LocationActionManager getModelManager();

    Handler getDelayedHandler();

    ContactsHelper getContactsHelper();

    LocationQueryFlowManager getLocationQueryFlowManager();

    String getLocationRequestCommand();

    boolean requestSingleLocationUpdate(String provider, LocationAction action);
}
