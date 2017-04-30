package info.alkor.whereareyou.logic;

import android.support.annotation.NonNull;

import info.alkor.whereareyou.location.LocationFormatter;
import info.alkor.whereareyou.location.StreetViewLocationFormatter;

/**
 * Location response encoder.
 * Created by Marlena on 2017-04-01.
 */
public class LocationResponseEncoder {

	private final LocationFormatter formatter = new LocationFormatter();
	private final StreetViewLocationFormatter formatter2 = new StreetViewLocationFormatter();

	public
	@NonNull
	String encodeLocationResponse(@NonNull LocationResponse locationResponse) {
		return formatter.format(locationResponse.getLocation());
	}

	public String encodeStreetViewLocationResponse(LocationResponse locationResponse) {
		return formatter2.format(locationResponse.getLocation());
	}
}
