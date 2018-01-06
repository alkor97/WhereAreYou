package info.alkor.whereareyou.ui.comparators;

import java.util.Comparator;

import info.alkor.whereareyou.model.LocationAction;

/**
 * Liocation action comparator.
 * Created by Marlena on 2017-11-06.
 */
public class LocationActionComparator implements Comparator<LocationAction> {

    public static Comparator<LocationAction> create() {
        return new LocationActionComparator();
    }

    public int compare(final LocationAction a, final LocationAction b) {
        return (int) (b.getActionId() - a.getActionId());
    }
}
