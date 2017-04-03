package info.alkor.whereareyou.logic.handlers.common;

import android.support.annotation.NonNull;

import info.alkor.whereareyou.logic.LocationRequest;

/**
 * Location request handler interface.
 * Created by Marlena on 2017-03-30.
 */
public interface LocationRequestHandler {
	/**
	 * Handle location request and return response via provided handler.
	 *
	 * @param request         location request
	 * @param responseHandler location response handler
	 */
	void handleLocationRequest(@NonNull LocationRequest request, @NonNull LocationResponseHandler
			responseHandler);
}
