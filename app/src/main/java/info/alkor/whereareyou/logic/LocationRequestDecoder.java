package info.alkor.whereareyou.logic;

import android.support.annotation.NonNull;
import android.telephony.SmsMessage;

import java.util.HashMap;
import java.util.Map;

import info.alkor.whereareyou.R;
import info.alkor.whereareyou.common.Requirements;

/**
 * Decoder of incoming SMS-based location requests.
 * Created by Marlena on 2017-03-30.
 */
public class LocationRequestDecoder {

	private final Map<String, LocationRequest.Type> mapping = new HashMap<>();

	public LocationRequestDecoder(@NonNull ExecutionContext context) {
		mapping.put(context.getString(R.string.one_time_location_request), LocationRequest.Type
				.SINGLE);
	}

	@Requirements({"UC-DECODE-LOCATION-REQUEST"})
	public
	@NonNull
	LocationRequest decodeLocationRequest(@NonNull SmsMessage message) {
		String request = getBody(message);
		LocationRequest.Type type = parse(request);
		return new LocationRequest(message.getOriginatingAddress(), type);
	}

	private LocationRequest.Type parse(String request) {
		LocationRequest.Type type = mapping.get(request);
		return type != null ? type : LocationRequest.Type.UNKNOWN;
	}

	private String getBody(SmsMessage message) {
		return message.getMessageBody().trim();
	}
}
