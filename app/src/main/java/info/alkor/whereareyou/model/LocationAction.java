package info.alkor.whereareyou.model;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;

/**
 * Location request.
 */
public class LocationAction implements Serializable {

    private final LocationActionSide side;
    private long actionId = 0;
    private State state = State.UNINITIALIZED;
    private Location location;
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;

    public LocationAction(@NonNull LocationActionSide side) {
        this.actionId = nextId();
        this.side = side;
    }

    LocationAction(long actionId) {
        this.actionId = actionId;
        this.side = null;
    }

    private static long nextId() {
        return System.currentTimeMillis();
    }

    private String name() {
        return "action " + actionId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        Log.i(name(), "state " + state);
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
        Log.i(name(), "location set");
    }

    public long getLocationTime() {
        return location != null ? location.getTime() : 0;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
        Log.i(name(), "delivery " + deliveryStatus);
    }

    public enum State {
        UNINITIALIZED, QUERIED, ANSWERED
    }

    public enum DeliveryStatus {
        PENDING, SENT, DELIVERED
    }
}
