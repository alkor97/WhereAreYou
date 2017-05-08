package info.alkor.whereareyou.logic;

import android.support.annotation.NonNull;

import info.alkor.whereareyou.batery.BatteryLevel;
import info.alkor.whereareyou.batery.BatteryLevelFormatter;
import info.alkor.whereareyou.location.LocationFormatter;
import info.alkor.whereareyou.location.StreetViewLocationFormatter;

/**
 * Location response encoder.
 * Created by Marlena on 2017-04-01.
 */
public class LocationResponseEncoder {

	private final LocationFormatter formatter = new LocationFormatter();
	private final StreetViewLocationFormatter formatter2 = new StreetViewLocationFormatter();
	private final BatteryLevelFormatter batteryLevelFormatter = new BatteryLevelFormatter();

	public
	@NonNull
	String encodeLocationResponse(@NonNull LocationResponse locationResponse, BatteryLevel
			batteryLevel) {
		return format(formatter.format(locationResponse.getLocation()), batteryLevelFormatter
				.format(batteryLevel));
	}

	public String encodeStreetViewLocationResponse(LocationResponse locationResponse, BatteryLevel
			batteryLevel) {
		return format(formatter2.format(locationResponse.getLocation()), batteryLevelFormatter
				.format(batteryLevel));
	}

	private String format(String location, String batteryLevel) {
		return String.format("%s %s", location, batteryLevel);
	}
}
