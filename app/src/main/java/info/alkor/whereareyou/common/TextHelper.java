package info.alkor.whereareyou.common;

import java.net.URLEncoder;

/**
 * Text helper.
 * Created by Marlena on 2017-12-30.
 */
public class TextHelper {

    public String formatPhone(String phone) {
        phone = phone.replaceAll("\\s+", "");
        StringBuilder result = new StringBuilder();
        int last = 0;
        for (int i = 0; i < phone.length() / 3; ++i) {
            int start = last = phone.length() - (i + 1) * 3;
            int end = start + 3;
            if (result.length() > 0) {
                result.insert(0, ' ');
            }
            result.insert(0, phone.substring(start, end));
        }
        result.insert(0, phone.substring(0, last));
        return result.toString();
    }

    public String encode(String value) {
        return value != null ? URLEncoder.encode(value) : "";
    }

    public String normalizePhone(String phone) {
        return phone.replaceAll("\\s+", "").replaceAll("^0+", "+");
    }
}
