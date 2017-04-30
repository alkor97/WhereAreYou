package info.alkor.whereareyou.location;

import android.location.Location;

import java.util.Locale;

/**
 * Street View location formatter.
 * Created by Marlena on 2017-04-29.
 */
public class StreetViewLocationFormatter extends LocationFormatter {
	protected String getGoogleMapsLink(Location location) {
		return String.format(Locale.US, "http://maps.google.com/maps?layer=c&cbll=%.8f," +
				"%.8f&cbp=11,%.0f,0,0,0", location.getLatitude(), location.getLongitude(),
				location.getBearing());
	}
}
