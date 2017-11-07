package info.alkor.whereareyou.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import info.alkor.whereareyou.ui.comparators.LocationActionComparator;

/**
 * Location requests container.
 * Created by Marlena on 2017-07-14.
 */
public class LocationActionList {

    private final List<LocationAction> actions = new ArrayList<>();
    private final Comparator<LocationAction> comparator = LocationActionComparator.create();
    private final Comparator<LocationAction> providerComparator = LocationActionComparator.createSideComparator();

    public int addAction(LocationAction action) {
        final int idx = Collections.binarySearch(actions, action, comparator);
        if (idx < 0) {
            actions.add(-idx - 1, action);
            return -idx - 1;
        } else {
            actions.set(idx, action);
            return idx;
        }
    }

    public LocationAction find(long id, LocationActionSide provider) {
        final int idx = Collections.binarySearch(actions, new LocationAction(id, provider), providerComparator);
        return idx >= 0 ? actions.get(idx) : null;
    }

    public int size() {
        return actions.size();
    }

    public LocationAction get(int idx) {
        return actions.get(idx);
    }
}
