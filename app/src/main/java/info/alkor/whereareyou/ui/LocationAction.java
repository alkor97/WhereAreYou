package info.alkor.whereareyou.ui;

import android.location.Location;

import java.util.Date;

/**
 * Location request.
 */
public class LocationAction {
    private final long actionId;
    private final LocationActionSide side;
    private State state;
    private Location location;

    public static enum State {
        UNINITIALIZED, QUERIED, ANSWERED
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public LocationAction(long actionId, LocationActionSide side) {
        this.actionId = actionId;
        this.side = side;
    }

    public LocationAction(LocationActionSide side) {
        this(System.currentTimeMillis(), side);
    }

    public LocationActionSide getSide() {
        return side;
    }

    public long getActionId() {
        return actionId;
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
}
