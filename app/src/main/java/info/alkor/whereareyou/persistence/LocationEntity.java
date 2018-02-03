package info.alkor.whereareyou.persistence;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Location entity.
 * Created by Marlena on 2018-01-28.
 */
public class LocationEntity {

    public long time;

    @NonNull
    public String provider = "gps";
    @Nullable
    public Double altitude;
    @Nullable
    public Float accuracy;
    @Nullable
    public Float bearing;
    @Nullable
    public Float speed;
    double latitude;
    double longitude;
}
