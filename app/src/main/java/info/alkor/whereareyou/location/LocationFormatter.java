package info.alkor.whereareyou.location;

import android.location.Location;
import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * Response formatter.
 * Created by Marlena on 2017-03-28.
 */
public class LocationFormatter {
	public String getGoogleMapsLocationLink(@NonNull Location location) {
		return String.format(Locale.US, "https://www.google.com/maps/@%.8f,%.8f,13.0z", location
				.getLatitude(), location.getLongitude());
	}
}
