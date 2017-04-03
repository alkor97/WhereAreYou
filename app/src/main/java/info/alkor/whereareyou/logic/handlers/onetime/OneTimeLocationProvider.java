package info.alkor.whereareyou.logic.handlers.onetime;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import info.alkor.whereareyou.location.DefaultLocationListener;
import info.alkor.whereareyou.location.SimpleLocationProvider;
import info.alkor.whereareyou.logic.LocationRequest;
import info.alkor.whereareyou.logic.LocationResponse;
import info.alkor.whereareyou.logic.handlers.common.LocationRequestHandler;
import info.alkor.whereareyou.logic.handlers.common.LocationResponseHandler;
import info.alkor.whereareyou.settings.ApplicationSettings;

/**
 * Single location request provider.
 * Created by Marlena on 2017-03-30.
 */
public class OneTimeLocationProvider implements LocationRequestHandler {

	private final ApplicationSettings settings;
	private final SimpleLocationProvider locationProvider;
	private final AtomicBoolean sendingStarted = new AtomicBoolean(false);
	private final AtomicBoolean cancellingInitiated = new AtomicBoolean(false);
	private final Handler async = new Handler();
	private LocationListener locationListener;
	private Location mostAccurateLocation;
	private Runnable cancellingJob;

	public OneTimeLocationProvider(ApplicationSettings settings, LocationManager locationManager) {
		this.settings = settings;
		this.locationProvider = new SimpleLocationProvider(locationManager, settings
				.getLocationSettings());
	}

	@Override
	public void handleLocationRequest(@NonNull final LocationRequest request, @NonNull final
	LocationResponseHandler responseHandler) {
		// create location listener if not exists
		if (locationListener == null) {
			locationListener = new DefaultLocationListener() {
				@Override
				public void onLocationChanged(Location location) {
					Log.i("outgoing", "location provided from " + location.getProvider());
					// this can be called for every requested location provider
					locationProvided(request, location, responseHandler);
				}
			};
		}

		// requests location updates
		locationProvider.requestLocationUpdates(locationListener, 1, 0.01f);
		Log.i("incoming", "location requested");

		// ensure no more than one canceling job is scheduled
		if (!cancellingInitiated.getAndSet(true)) {
			// ensure that location updates are valid for at most minimal time between updates
			async.postDelayed(cancellingJob = new Runnable() {
				@Override
				public void run() {
					cancelLocationUpdates();
				}
			}, settings.getLocationSettings().getMinTimeInterval(TimeUnit.MILLISECONDS));
		}
	}

	/**
	 * This method is called by every location provider on location available.
	 *
	 * @param request         location request
	 * @param location        actual location
	 * @param responseHandler response handler
	 */
	private void locationProvided(final LocationRequest request, Location location, final
	LocationResponseHandler responseHandler) {
		// compare accuracy delivered location against stored one
		if (mostAccurateLocation == null || location.getAccuracy() < mostAccurateLocation
				.getAccuracy()) {
			mostAccurateLocation = location;
		}

		// cancel further location updates, as this class provides only one-time location
		cancelLocationUpdates();

		// ensure sending handler is set only once for location request
		if (!sendingStarted.getAndSet(true)) {
			// wait for some time until most accurate location is provided and send response to
			// location requester
			async.postDelayed(new Runnable() {
				@Override
				public void run() {
					handleResponse(request, mostAccurateLocation, responseHandler);
				}
			}, settings.getLocationSettings().getMaxAwaitTimeForBetterAccuracyLocation(TimeUnit
					.MILLISECONDS));
		}
	}

	private void cancelLocationUpdates() {
		if (locationListener != null) {
			locationProvider.cancelLocationUpdates(locationListener);
			locationListener = null;

			// remove cancelling job if any
			if (cancellingJob != null) {
				async.removeCallbacks(cancellingJob);
				cancellingJob = null;
			}
		}
	}

	private void handleResponse(LocationRequest request, Location location,
	                            LocationResponseHandler responseHandler) {
		responseHandler.handleLocationResponse(toLocationResponse(request, location));
	}

	private LocationResponse toLocationResponse(LocationRequest request, Location location) {
		return new LocationResponse(request, location);
	}
}
