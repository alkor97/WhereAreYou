package info.alkor.whereareyou.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Default location listener.
 * Created by Marlena on 2017-03-28.
 */
public class DefaultLocationListener implements LocationListener {
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}
}
