package info.alkor.whereareyou.batery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import info.alkor.whereareyou.logic.ExecutionContext;

/**
 * Provider of battery information.
 * Created by Marlena on 2017-03-07.
 */
public class BatteryLevelProvider {

	private final Context context;
	private Intent batteryStatus;

	public BatteryLevelProvider(@NonNull ExecutionContext context) {
		this.context = context.getContext();
	}

	public BatteryLevel getBatteryLevel() {
		return new BatteryLevel(getBatteryStatus());
	}

	private Intent getBatteryStatus() {
		if (batteryStatus == null) {
			batteryStatus = context.registerReceiver(null, new IntentFilter(Intent
					.ACTION_BATTERY_CHANGED));
		}
		return batteryStatus;
	}
}
