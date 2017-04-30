package info.alkor.whereareyou.logic.handlers.onetime;

import android.location.Location;
import android.os.Handler;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import info.alkor.whereareyou.location.DefaultLocationListener;
import info.alkor.whereareyou.logic.LocationRequest;
import info.alkor.whereareyou.logic.LocationResponse;
import info.alkor.whereareyou.logic.handlers.common.LocationResponseHandler;
import info.alkor.whereareyou.settings.ApplicationSettings;
import info.alkor.whereareyou.settings.CustomLogger;

/**
 * One time location listener.
 * Created by Marlena on 2017-04-29.
 */
class OneTimeLocationListener extends DefaultLocationListener {

	private final ApplicationSettings settings;
	private final LocationRequest request;
	private final LocationResponseHandler responseHandler;
	private final Handler async = new Handler();
	private final AtomicBoolean sendingStarted = new AtomicBoolean(false);
	private Location mostAccurateLocation;
	private Runnable handleResponseJob;

	OneTimeLocationListener(ApplicationSettings settings, LocationRequest request,
	                        LocationResponseHandler responseHandler) {
		this.settings = settings;
		this.request = request;
		this.responseHandler = responseHandler;
	}

	@Override
	public void onLocationChanged(Location location) {
		CustomLogger.outgoing("got location from " + location.getProvider());

		synchronized (this) {
			// compare accuracy delivered location against stored one
			if (mostAccurateLocation == null || location.getAccuracy() < mostAccurateLocation
					.getAccuracy()) {
				mostAccurateLocation = location;
			}
		}

		initiateResponseSending();
	}

	private void initiateResponseSending() {
		// ensure sending handler is set only once for location request
		if (!sendingStarted.getAndSet(true)) {
			// wait for some time until most accurate location is provided and send response to
			// location requester
			async.postDelayed(handleResponseJob = new Runnable() {
				@Override
				public void run() {
					async.removeCallbacks(handleResponseJob);
					handleResponseJob = null;
					handleResponse(request, mostAccurateLocation, responseHandler);
				}
			}, settings.getLocationSettings().getMaxAwaitTimeForBetterLocationAccuracy(TimeUnit
					.MILLISECONDS));
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
