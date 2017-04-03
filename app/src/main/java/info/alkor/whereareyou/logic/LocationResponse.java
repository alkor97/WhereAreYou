package info.alkor.whereareyou.logic;

import android.location.Location;

/**
 * Location response.
 * Created by Marlena on 2017-03-30.
 */
public class LocationResponse {

	private final LocationRequest request;
	private final Location location;

	public LocationResponse() {
		this(null, null);
	}

	public LocationResponse(LocationRequest request, Location location) {
		this.request = request;
		this.location = location;
	}

	public String getDestinationAddress() {
		return request.getOriginAddress();
	}

	public Location getLocation() {
		return location;
	}

	public boolean isValid() {
		return request != null && location != null;
	}
}
