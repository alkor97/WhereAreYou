package info.alkor.whereareyou.settings;

/**
 * Created by Marlena on 2017-02-19.
 */

public class ApplicationSettings {

	private final LocationSettings locationSettings;

	public ApplicationSettings(LocationSettings locationSettings) {
		this.locationSettings = locationSettings;
	}

	public LocationSettings getLocationSettings() {
		return locationSettings;
	}
}
