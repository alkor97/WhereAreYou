package info.alkor.whereareyou.settings;

/**
 * Application settings.
 * Created by Marlena on 2017-02-19.
 */
public class ApplicationSettings {

	private final LocationSettings locationSettings;
	private final UserManager userManager;

	public ApplicationSettings(LocationSettings locationSettings, UserManager userManager) {
		this.locationSettings = locationSettings;
		this.userManager = userManager;
	}

	public LocationSettings getLocationSettings() {
		return locationSettings;
	}

	public UserManager getUserManager() {
		return userManager;
	}
}
