package info.alkor.whereareyou.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import info.alkor.whereareyou.settings.DistanceUnit;
import info.alkor.whereareyou.settings.LocationSettings;

/**
 * Created by Marlena on 2017-02-20.
 */
public class LocationProvider {

	private final AppCompatActivity activity;
	private final LocationSettings settings;

	private Location lastKnownLocation;
	private final LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			lastKnownLocation = location;
			LocationProvider.this.onLocationProvided(location);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			activity.startActivity(intent);
		}
	};

	public LocationProvider(@NonNull AppCompatActivity activity, @NonNull LocationSettings
			settings) {
		this.activity = activity;
		this.settings = settings;
	}

	@SuppressWarnings("MissingPermission")
	public void requestLocationUpdates() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			Set<String> deniedPermissions = getDeniedPermissions();
			if (!deniedPermissions.isEmpty()) {
				activity.requestPermissions(deniedPermissions.toArray(new
						String[deniedPermissions.size()]), hashCode());
				return;
			}
		}

		final long minTimeInterval = settings.getMinTimeInterval(TimeUnit.MILLISECONDS);
		final float minDistance = settings.getMInDistanceInterval(DistanceUnit.METERS);

		for (String provider : settings.getLocationProviders()) {
			getLocationManager().requestLocationUpdates(provider, minTimeInterval,
					minDistance, locationListener);
		}
	}

	@SuppressWarnings("MissingPermission")
	public void cancelLocationUpdates() {
		getLocationManager().removeUpdates(locationListener);
	}

	private Set<String> getDeniedPermissions() {
		Set<String> deniedPermissions = new HashSet<>();
		for (String provider : settings.getLocationProviders()) {
			final String permission = getRequiredPermission(provider);
			if (permission != null && isPermissionDenied(permission)) {
				deniedPermissions.add(permission);
			}
		}
		return deniedPermissions;
	}

	private String getRequiredPermission(String locationProvider) {
		switch (locationProvider) {
			case LocationManager.GPS_PROVIDER:
				return Manifest.permission.ACCESS_FINE_LOCATION;
			case LocationManager.NETWORK_PROVIDER:
				return Manifest.permission.ACCESS_COARSE_LOCATION;
		}
		return null;
	}

	private boolean isPermissionGranted(@NonNull String permission) {
		return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager
				.PERMISSION_GRANTED;
	}

	private boolean isPermissionDenied(@NonNull String permission) {
		return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager
				.PERMISSION_DENIED;
	}

	private LocationManager getLocationManager() {
		return (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
	}

	public Location getLastKnownLocation() {
		return lastKnownLocation;
	}

	public void onLocationProvided(Location location) {
	}
}
