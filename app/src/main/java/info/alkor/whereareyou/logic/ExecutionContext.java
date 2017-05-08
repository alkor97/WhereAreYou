package info.alkor.whereareyou.logic;

import android.content.Context;
import android.location.LocationManager;

import info.alkor.whereareyou.settings.ApplicationSettings;

/**
 * Execution context providing required system services.
 * Created by Marlena on 2017-04-01.
 */
public interface ExecutionContext {
	String getString(int resId);

	LocationManager getLocationManager();

	ApplicationSettings getApplicationSettings();

	Context getContext();
}
