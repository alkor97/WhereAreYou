package info.alkor.whereareyou.settings;

/**
 * Custom logger.
 * Created by Marlena on 2017-04-26.
 */
public class CustomLogger {

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
		sb.append(tag).append(' ').append(message).append('\n');
	}
}
