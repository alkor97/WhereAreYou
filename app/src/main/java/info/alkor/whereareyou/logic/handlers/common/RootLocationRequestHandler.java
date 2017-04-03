package info.alkor.whereareyou.logic.handlers.common;

import android.support.annotation.NonNull;
import android.telephony.SmsMessage;

import info.alkor.whereareyou.logic.ExecutionContext;
import info.alkor.whereareyou.logic.LocationRequest;
import info.alkor.whereareyou.logic.LocationRequestDecoder;
import info.alkor.whereareyou.logic.handlers.onetime.OneTimeLocationProvider;
import info.alkor.whereareyou.settings.UserManager;

/**
 * Location request handler.
 * Created by Marlena on 2017-03-30.
 */
public class RootLocationRequestHandler {

	private final ExecutionContext context;
	private final LocationRequestDecoder decoder;
	private LocationRequestHandler defaultHandler = new DefaultLocationRequestHandler();
	private LocationResponseHandler responseHandler;

	public RootLocationRequestHandler(@NonNull ExecutionContext context) {
		this.context = context;
		this.decoder = new LocationRequestDecoder(context);
	}

	public void handleLocationRequest(@NonNull SmsMessage message) {
		LocationRequest request = decoder.decodeLocationRequest(message);
		// verify that location requester is on white list
		if (getUserManager().canAccessLocation(request.getOriginAddress())) {
			LocationRequestHandler requestHandler = getLocationRequestHandler(request);
			LocationResponseHandler responseHandler = getLocationResponseHandler();
			requestHandler.handleLocationRequest(request, responseHandler);
		}
	}

	private LocationResponseHandler getLocationResponseHandler() {
		if (responseHandler == null) {
			responseHandler = new DefaultLocationResponseHandler(context.getApplicationSettings());
		}
		return responseHandler;
	}

	private LocationRequestHandler getLocationRequestHandler(LocationRequest request) {
		if (LocationRequest.Type.SINGLE == request.getType()) {
			return new OneTimeLocationProvider(context.getApplicationSettings(), context
					.getLocationManager());
		}
		if (defaultHandler == null) {
			defaultHandler = new DefaultLocationRequestHandler();
		}
		return defaultHandler;
	}

	private UserManager getUserManager() {
		return context.getApplicationSettings().getUserManager();
	}
}
