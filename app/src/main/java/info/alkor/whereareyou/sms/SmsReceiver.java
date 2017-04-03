package info.alkor.whereareyou.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import info.alkor.whereareyou.common.Requirements;
import info.alkor.whereareyou.logic.ExecutionContext;
import info.alkor.whereareyou.logic.handlers.common.RootLocationRequestHandler;
import info.alkor.whereareyou.settings.ApplicationSettings;
import info.alkor.whereareyou.settings.LocationSettings;
import info.alkor.whereareyou.settings.UserManager;

/**
 * SMS receiver.
 * Created by Marlena on 2017-03-24.
 */
public class SmsReceiver extends BroadcastReceiver {

	@Requirements({"UC-RECEIVE-SMS"})
	@Override
	public void onReceive(final Context context, Intent intent) {
		final SmsMessage message = getMessage(intent);
		if (message != null) {
			getLocationRequestHandler(context).handleLocationRequest(message);
		}
	}

	private RootLocationRequestHandler getLocationRequestHandler(Context context) {
		return new RootLocationRequestHandler(getExecutionContext(context));
	}

	private ExecutionContext getExecutionContext(final Context context) {
		return new ExecutionContext() {
			@Override
			public String getString(int resId) {
				return context.getString(resId);
			}

			@Override
			public LocationManager getLocationManager() {
				return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			}

			@Override
			public ApplicationSettings getApplicationSettings() {
				return new ApplicationSettings(new LocationSettings(), new UserManager());
			}
		};
	}

	private SmsMessage getMessage(Intent intent) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
			return messages[0];
		} else {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				final Object[] pdus = (Object[]) bundle.get("pdus");
				if (pdus != null && pdus.length > 0) {
					return SmsMessage.createFromPdu((byte[]) pdus[0]);
				}
			}
		}
		return null;
	}
}
