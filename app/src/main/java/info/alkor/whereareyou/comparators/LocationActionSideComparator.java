package info.alkor.whereareyou.comparators;

import java.util.Comparator;

import info.alkor.whereareyou.model.LocationActionSide;

/**
 * Location action side comparator.
 * Created by Marlena on 2017-11-06.
 */
public class LocationActionSideComparator implements Comparator<LocationActionSide> {

    private final Comparator<LocationActionSide.Type> typeComparator = NullAwareComparator.create(LocationActionSide.Type.class);
    private final Comparator<String> comparator = NullAwareComparator.create(String.class);

    public static Comparator<LocationActionSide> create() {
        return NullAwareComparator.create(new LocationActionSideComparator());
    }

    public int compare(LocationActionSide a, LocationActionSide b) {
        final int typeComparison = typeComparator.compare(a.getType(), b.getType());
        if (typeComparison == 0) {
            final int nameComparison = comparator.compare(a.getName(), b.getName());
            if (nameComparison == 0) {
                return comparator.compare(a.getPhone(), b.getPhone());
            }
            return nameComparison;
        } else {
            return typeComparison;
        }
    }
}
