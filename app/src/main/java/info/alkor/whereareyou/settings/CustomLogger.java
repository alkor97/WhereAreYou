package info.alkor.whereareyou.settings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Custom logger.
 * Created by Marlena on 2017-04-26.
 */
public class CustomLogger {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss", Locale.US);
	private static StringBuilder sb;

	public static void clear() {
		sb = new StringBuilder("=== LOGS ===\n");
	}

	public static String get() {
		if (sb == null) {
			clear();
		}
		return sb.toString();
	}

	public static void incoming(String message) {
		log("incoming", message);
	}

	public static void outgoing(String message) {
		log("outgoing", message);
	}

	public static void requesting(String message) {
		log("requesting", message);
	}

	private static void log(String tag, String message) {
		if (sb == null) {
			clear();
		}
		sb.append(DATE_FORMAT.format(new Date())).append(' ').append(tag).append(' ').append
				(message).append('\n');
	}
}
