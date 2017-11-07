package info.alkor.whereareyou.ui.comparators;

import java.util.Comparator;

import info.alkor.whereareyou.ui.LocationAction;
import info.alkor.whereareyou.ui.LocationActionSide;

/**
 * Created by Marlena on 2017-11-06.
 */

public class LocationActionComparator implements Comparator<LocationAction> {

    private final Comparator<LocationActionSide> sideComparator;
    private final Comparator<LocationAction.State> stateComparator;

    private LocationActionComparator(Comparator<LocationActionSide> sideComparator, Comparator<LocationAction.State> stateComparator) {
        this.sideComparator = sideComparator;
        this.stateComparator = stateComparator;
    }

    public int compare(LocationAction a, LocationAction b) {
        final long id = a.getActionId() - b.getActionId();
        if (id == 0) {
            final int sideComparison = sideComparator.compare(a.getSide(), b.getSide());
            return sideComparison == 0 ? stateComparator.compare(a.getState(), b.getState()) : sideComparison;
        } else {
            return id < 0 ? -1 : 1;
        }
    }

    public static Comparator<LocationAction> create() {
        final Comparator<LocationActionSide> sideComparator = LocationActionSideComparator.create();
        return new LocationActionComparator(sideComparator, NullAwareComparator.create(LocationAction.State.class));
    }

    public static Comparator<LocationAction> createSideComparator() {
        return new LocationActionComparator(
                LocationActionSideComparator.create(),
                DummyComparator.create(LocationAction.State.class, 0));
    }
}
