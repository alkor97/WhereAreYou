package info.alkor.whereareyou.logic.handlers.common;

import android.support.annotation.NonNull;

import info.alkor.whereareyou.logic.ExecutionContext;
import info.alkor.whereareyou.logic.LocationResponse;
import info.alkor.whereareyou.logic.LocationResponseEncoder;
import info.alkor.whereareyou.settings.CustomLogger;
import info.alkor.whereareyou.settings.UserManager;
import info.alkor.whereareyou.sms.SmsSender;

/**
 * Default location response handler.
 * Created by Marlena on 2017-04-01.
 */
class DefaultLocationResponseHandler implements LocationResponseHandler {

	private final SmsSender sender = new SmsSender();
	private final LocationResponseEncoder encoder = new LocationResponseEncoder();
	private final UserManager userManager;

	DefaultLocationResponseHandler(@NonNull ExecutionContext context) {
		this.userManager = context.getApplicationSettings().getUserManager();
	}

	@Override
	public void handleLocationResponse(@NonNull LocationResponse response) {
		if (userManager.canAccessLocation(response.getDestinationAddress())) {
			sender.send(response.getDestinationAddress(), encoder.encodeLocationResponse
					(response));
			if (response.getLocation().hasBearing()) {
				sender.send(response.getDestinationAddress(), encoder
						.encodeStreetViewLocationResponse(response));
			}
		} else {
			CustomLogger.outgoing("destination not on white list, ignoring");
		}
	}
}
