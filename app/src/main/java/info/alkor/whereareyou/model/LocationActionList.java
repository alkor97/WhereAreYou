package info.alkor.whereareyou.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import info.alkor.whereareyou.ui.comparators.LocationActionComparator;

/**
 * Location requests container.
 * Created by Marlena on 2017-07-14.
 */
public class LocationActionList {

    private final List<LocationAction> actions = new ArrayList<>();
    private final Comparator<LocationAction> comparator = LocationActionComparator.create();

    public int addAction(LocationAction action) {
        int position = Collections.binarySearch(actions, action, comparator);
        if (position < 0) {
            actions.add(-position - 1, action);
        } else {
            actions.set(position, action);
        }
        return position;
    }

    public LocationAction findRelatedNotFulfilledAction(LocationActionSide provider) {
        for (LocationAction locationAction : actions) {
            if (locationAction.getState() == LocationAction.State.QUERIED && locationAction.getSide().getPhone().equals(provider.getPhone())) {
                return locationAction;
            }
        }
        return null;
    }

    public LocationAction find(long actionId) {
        int position = Collections.binarySearch(actions, new LocationAction(actionId), comparator);
        if (position >= 0) {
            return actions.get(position);
        }
        return null;
    }

    public int size() {
        return actions.size();
    }

    public LocationAction get(int idx) {
        return actions.get(idx);
    }
}
