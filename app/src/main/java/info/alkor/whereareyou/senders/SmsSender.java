package info.alkor.whereareyou.senders;

import android.support.annotation.NonNull;
import android.telephony.SmsManager;

import info.alkor.whereareyou.common.Requirements;

/**
 * SMS sending utility.
 * Created by Marlena on 2017-03-28.
 */
public class SmsSender {

	private final SmsManager manager = SmsManager.getDefault();

	/**
	 * Send SMS with provided content to specified destination.
	 * @param destination phone number of message destination
	 * @param content     content of message
	 */
	@Requirements({"UC-SEND-SMS"})
	public void send(@NonNull String destination, @NonNull String content) {
		manager.sendTextMessage(destination, null, content, null, null);
	}
}
