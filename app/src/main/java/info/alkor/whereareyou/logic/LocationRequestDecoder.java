package info.alkor.whereareyou.logic;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
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
	private final ExecutionContext context;

	public LocationRequestDecoder(@NonNull ExecutionContext context) {
	    this.context = context;
		mapping.put(context.getString(R.string.one_time_location_request), LocationRequest.Type
				.SINGLE);
	}

	@Requirements({"UC-DECODE-LOCATION-REQUEST"})
	public
	@NonNull
	LocationRequest decodeLocationRequest(@NonNull SmsMessage message) {
		String request = getBody(message);
		LocationRequest.Type type = parse(request);
		return new LocationRequest(message.getOriginatingAddress(), getDisplayName(message), type);
	}

	private LocationRequest.Type parse(String request) {
		LocationRequest.Type type = mapping.get(request);
		return type != null ? type : LocationRequest.Type.RESPONSE;
	}

	private String getBody(SmsMessage message) {
		return message.getMessageBody().trim();
	}

	private String getDisplayName(SmsMessage message) {
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(message.getOriginatingAddress()));
		Cursor cursor = null;
		try {
			cursor = context.getContext().getContentResolver().query(uri, null, null, null, null);
			if (cursor != null) {
				final int displayNameIdx = cursor.getColumnIndex(ContactsContract
						.CommonDataKinds.Phone.DISPLAY_NAME);

				if (cursor.moveToFirst()) {
					return cursor.getString(displayNameIdx);
				}
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return "";
	}
}
