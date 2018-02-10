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
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;

    public LocationAction(long actionId, @NonNull LocationActionSide side, @NonNull State state) {
        this.actionId = actionId;
        this.side = side;
        this.state = state;
    }

    public State getState() {
        return state;
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

    public long getLocationTime() {
        return location != null ? location.getTime() : 0;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public enum State {
        UNINITIALIZED, QUERIED, ANSWERED
    }

    public enum DeliveryStatus {
        PENDING, SENT, DELIVERED
    }
}
