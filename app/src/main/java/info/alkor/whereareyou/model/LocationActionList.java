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
    private AtomicLong generator = new AtomicLong(0);

    public int addAction(LocationAction action) {
        if (action.getActionId() == 0) {
            action.setActionId(nextId());
        }
        int position = Collections.binarySearch(actions, action, comparator);
        if (position < 0) {
            actions.add(-position - 1, action);
        } else {
            actions.set(position, action);
        }
        return position;
    }

    private long nextId() {
        long id;
        do {
            id = generator.incrementAndGet();
        } while (id == 0);
        return id;
    }

    public LocationAction findRelatedNotFulfilledAction(LocationActionSide provider) {
        for (LocationAction locationAction : actions) {
            if (locationAction.getState() == LocationAction.State.QUERIED && locationAction.getSide().getPhone().equals(provider.getPhone())) {
                return locationAction;
            }
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
