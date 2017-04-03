package info.alkor.whereareyou.batery;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Provider of battery information.
 * Created by Marlena on 2017-03-07.
 */
public class BatteryLevelProvider {

	private final AppCompatActivity activity;
	private Intent batteryStatus;

	public BatteryLevelProvider(AppCompatActivity activity) {
		this.activity = activity;
	}

	public float getCurrentBatteryLevel() {
		int level = getBatteryStatus().getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = getBatteryStatus().getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		return level / (float) scale;
	}

	private Intent getBatteryStatus() {
		if (batteryStatus == null) {
			batteryStatus = activity.registerReceiver(null, new IntentFilter(Intent
					.ACTION_BATTERY_CHANGED));
		}
		return batteryStatus;
	}
}
