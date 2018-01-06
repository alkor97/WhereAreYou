package info.alkor.whereareyou.settings;

import android.location.LocationManager;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Location-related settings.
 * Created by Marlena on 2017-02-19.
 */
public class LocationSettings {

    private long maxAwaitTimeForBetterLocationAccuracy = 15_000;

    @NonNull
    public Set<String> getLocationProviders() {
        return new HashSet<>(Arrays.asList(LocationManager.GPS_PROVIDER, LocationManager
                .NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER));
    }

    public void setMaxAwaitTimeForBetterLocationAccuracy(long value, @NonNull TimeUnit timeUnit) {
        this.maxAwaitTimeForBetterLocationAccuracy = TimeUnit.MILLISECONDS.convert(value, timeUnit);
    }

    public long getMaxAwaitTimeForBetterLocationAccuracy(@NonNull TimeUnit timeUnit) {
        return timeUnit.convert(maxAwaitTimeForBetterLocationAccuracy, TimeUnit.MILLISECONDS);
    }
}
