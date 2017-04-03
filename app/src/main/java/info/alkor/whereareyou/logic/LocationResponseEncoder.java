package info.alkor.whereareyou.logic;

import android.support.annotation.NonNull;

import info.alkor.whereareyou.location.LocationFormatter;

/**
 * Location response encoder.
 * Created by Marlena on 2017-04-01.
 */
public class LocationResponseEncoder {

	private final LocationFormatter formatter = new LocationFormatter();

	public
	@NonNull
	String encodeLocationResponse(@NonNull LocationResponse locationResponse) {
		return formatter.getGoogleMapsLocationLink(locationResponse.getLocation());
	}
}
