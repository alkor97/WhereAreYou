package info.alkor.whereareyou.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import info.alkor.whereareyou.ui.comparators.LocationActionComparator;

/**
 * Location requests container.
 * Created by Marlena on 2017-07-14.
 */
public class LocationActionList implements LocationActions {

    private final List<LocationAction> actions = new ArrayList<>();
    private final Comparator<LocationAction> comparator = LocationActionComparator.create();

    @Override
    public int addAction(@NonNull LocationAction action) {
        int position = Collections.binarySearch(actions, action, comparator);
        if (position < 0) {
            actions.add(-position - 1, action);
        } else {
            actions.set(position, action);
        }
        return position;
    }

    @Override
    public LocationAction findRelatedNotFulfilledAction(@NonNull LocationActionSide provider) {
        for (LocationAction locationAction : actions) {
            if (locationAction.getState() == LocationAction.State.QUERIED
                    && locationAction.getSide().equals(provider)) {
                return locationAction;
            }
        }
        return null;
    }

    @Override
    public LocationAction find(long actionId) {
        int position = Collections.binarySearch(actions, new LocationAction(actionId), comparator);
        if (position >= 0) {
            return actions.get(position);
        }
        return null;
    }

    @Override
    public int size() {
        return actions.size();
    }

    @Override
    public LocationAction get(int idx) {
        return actions.get(idx);
    }

    @Override
    public int removeAction(int idx) {
        if (0 <= idx && idx < actions.size()) {
            actions.remove(idx);
        }
        return idx;
    }
}
