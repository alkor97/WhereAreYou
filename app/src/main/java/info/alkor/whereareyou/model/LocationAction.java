package info.alkor.whereareyou.model;

import android.location.Location;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Location request.
 */
public class LocationAction implements Serializable {

    private final LocationActionSide side;
    private long actionId = 0;
    private State state = State.UNINITIALIZED;
    private Location location;

    public LocationAction(@NonNull LocationActionSide side) {
        this.side = side;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public LocationActionSide getSide() {
        return side;
    }

    public long getActionId() {
        return actionId;
    }

    void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public String getDisplayName() {
        return side.getName();
    }

    public String getPhoneNumber() {
        return side.getPhone();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getLocationTime() {
        return location != null ? location.getTime() : 0;
    }

    public enum State {
        UNINITIALIZED, QUERIED, ANSWERED
    }
}
