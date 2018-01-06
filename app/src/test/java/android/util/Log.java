package android.util;

/**
 * Test-only log implementation.
 * Created by Marlena on 2018-01-01.
 */
public class Log {
    public static int e(String tag, String msg) {
        System.out.println("ERROR: " + tag + ": " + msg);
        return 0;
    }

    public static int i(String tag, String msg) {
        System.out.println("INFO: " + tag + ": " + msg);
        return 0;
    }
}
