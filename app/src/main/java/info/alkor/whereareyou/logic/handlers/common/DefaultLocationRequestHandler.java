package info.alkor.whereareyou.logic.handlers.common;

import android.support.annotation.NonNull;

import info.alkor.whereareyou.logic.LocationRequest;

/**
 * Default location request andler doing nothing.
 * Created by Marlena on 2017-03-30.
 */
class DefaultLocationRequestHandler implements LocationRequestHandler {
	@Override
	public void handleLocationRequest(@NonNull LocationRequest request, @NonNull
			LocationResponseHandler responseHandler) {
	}
}
