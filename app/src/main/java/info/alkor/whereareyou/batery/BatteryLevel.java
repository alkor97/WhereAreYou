package info.alkor.whereareyou.batery;

import android.content.Intent;
import android.os.BatteryManager;
import android.support.annotation.NonNull;

/**
 * Battery level.
 */
public class BatteryLevel {

	private final Intent batteryStatus;

	BatteryLevel(@NonNull Intent batteryStatus) {
		this.batteryStatus = batteryStatus;
	}

	float getBatteryLevel() {
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		return level / (float) scale;
	}
}
