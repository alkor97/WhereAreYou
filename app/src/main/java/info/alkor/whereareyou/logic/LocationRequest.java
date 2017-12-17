package info.alkor.whereareyou.logic;

import android.support.annotation.NonNull;

/**
 * Location request.
 * Created by Marlena on 2017-03-30.
 */
public class LocationRequest {

    private final String originAddress;
    private final String originName;
    private final Type type;

    LocationRequest(@NonNull String originAddress, @NonNull String originName, @NonNull Type type) {
        this.originAddress = originAddress;
        this.originName = originName;
        this.type = type;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public String getOriginName() {
        return originName;
    }

    public Type getType() {
        return type;
    }

    /**
     * Location request types.
     * Created by Marlena on 2017-04-01.
     */
    public enum Type {
        UNKNOWN, SINGLE, RESPONSE
    }
}
