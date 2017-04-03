package info.alkor.whereareyou.logic.handlers.common;

import android.support.annotation.NonNull;

import info.alkor.whereareyou.logic.LocationResponse;

/**
 * Handler of location responses.
 * Created by Marlena on 2017-04-01.
 */
public interface LocationResponseHandler {
	void handleLocationResponse(@NonNull LocationResponse response);
}
