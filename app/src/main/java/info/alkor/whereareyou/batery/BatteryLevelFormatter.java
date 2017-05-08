package info.alkor.whereareyou.batery;

import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * Battery level formatter.
 */
public class BatteryLevelFormatter {
	public String format(@NonNull BatteryLevel batteryLevel) {
		return String.format(Locale.US, "%d%%", (int) (100 * batteryLevel.getBatteryLevel()));
	}
}
