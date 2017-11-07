package info.alkor.whereareyou.ui.comparators;

import java.util.Comparator;

import info.alkor.whereareyou.ui.LocationActionSide;

/**
 * Created by Marlena on 2017-11-06.
 */

public class LocationActionSideComparator implements Comparator<LocationActionSide> {

    private final Comparator<String> comparator = NullAwareComparator.create(String.class);

    public int compare(LocationActionSide a, LocationActionSide b) {
        final int nameComparison = comparator.compare(a.getName(), b.getName());
        return nameComparison == 0 ? comparator.compare(a.getPhone(), b.getPhone()) : nameComparison;
    }

    public static Comparator<LocationActionSide> create() {
        return NullAwareComparator.create(new LocationActionSideComparator());
    }
}
