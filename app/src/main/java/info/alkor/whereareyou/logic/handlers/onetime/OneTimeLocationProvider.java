package info.alkor.whereareyou.logic.handlers.onetime;

import android.location.LocationManager;
import android.support.annotation.NonNull;

import info.alkor.whereareyou.location.SimpleLocationProvider;
import info.alkor.whereareyou.logic.LocationRequest;
import info.alkor.whereareyou.logic.handlers.common.LocationRequestHandler;
import info.alkor.whereareyou.logic.handlers.common.LocationResponseHandler;
import info.alkor.whereareyou.settings.ApplicationSettings;
import info.alkor.whereareyou.settings.CustomLogger;

/**
 * Single location request provider.
 * Created by Marlena on 2017-03-30.
 */
public class OneTimeLocationProvider implements LocationRequestHandler {

	private final ApplicationSettings settings;
	private final SimpleLocationProvider locationProvider;

	public OneTimeLocationProvider(ApplicationSettings settings, LocationManager locationManager) {
		this.settings = settings;
		this.locationProvider = new SimpleLocationProvider(locationManager, settings
				.getLocationSettings());
	}

	@Override
	public void handleLocationRequest(@NonNull final LocationRequest request, @NonNull final
	LocationResponseHandler responseHandler) {
		// create location listener if not exists
		final OneTimeLocationListener locationListener = new OneTimeLocationListener(settings,
				request, responseHandler);

		// requests location updates
		locationProvider.requestSingleUpdate(locationListener);
		CustomLogger.incoming("location requested");
	}
}
