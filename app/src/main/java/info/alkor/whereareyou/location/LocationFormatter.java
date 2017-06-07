package info.alkor.whereareyou.location;

import android.location.Location;
import android.support.annotation.NonNull;

/**
 * Location formatter.
 * Created by Marlena on 2017-06-07.
 */
public interface LocationFormatter {
    @NonNull
    String format(@NonNull Location location);
}
