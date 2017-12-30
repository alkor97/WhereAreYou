package info.alkor.whereareyou.location;

import android.location.Location;
import android.support.annotation.NonNull;

/**
 * Location parser.
 * Created by Marlena on 2017-06-07.
 */
public interface LocationParser {
    @NonNull
    Location parse(@NonNull String location) throws ParsingException;

    class ParsingException extends Exception {
        public ParsingException() {
        }

        public ParsingException(String message) {
            super(message);
        }

        public ParsingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
