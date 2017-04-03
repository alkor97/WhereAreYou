package info.alkor.whereareyou.location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import info.alkor.whereareyou.common.Requirements;
import info.alkor.whereareyou.settings.DistanceUnit;
import info.alkor.whereareyou.settings.LocationSettings;

/**
 * Simple location provider which assumes that application has proper privileges set.
 * Created by Marlena on 2017-03-28.
 */
public class SimpleLocationProvider {

	private final LocationManager locationManager;
	private final LocationSettings settings;

	public SimpleLocationProvider(@NonNull LocationManager locationManager, @NonNull
			LocationSettings settings) {
		this.locationManager = locationManager;
		this.settings = settings;
	}

	public void requestLocationUpdates(@NonNull LocationListener locationListener) {
		requestLocationUpdates(locationListener, settings.getMinTimeInterval(TimeUnit
				.MILLISECONDS), settings.getMInDistanceInterval(DistanceUnit.METERS));
	}

	@Requirements({"UC-REQUEST-LOCATION-UPDATES"})
	@SuppressWarnings("MissingPermission")
	public void requestLocationUpdates(@NonNull final LocationListener locationListener, long
			minTimeInterval, float minDistance) {
		for (String provider : settings.getLocationProviders()) {
			locationManager.requestLocationUpdates(provider, minTimeInterval, minDistance,
					locationListener);
		}
	}

	@Requirements({"UC-CANCEL-LOCATION-UPDATES"})
	@SuppressWarnings("MissingPermission")
	public void cancelLocationUpdates(LocationListener locationListener) {
		locationManager.removeUpdates(locationListener);
	}
}
