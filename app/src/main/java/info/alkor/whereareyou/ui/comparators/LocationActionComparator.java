package info.alkor.whereareyou.ui.comparators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationActionSide;

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

    public static Comparator<LocationAction> create() {
        final Comparator<LocationActionSide> sideComparator = LocationActionSideComparator.create();
        return new LocationActionComparator(sideComparator, NullAwareComparator.create(LocationAction.State.class));
    }

    public int compare(final LocationAction a, final LocationAction b) {
        return (int) (b.getActionId() - a.getActionId());
    }
}
