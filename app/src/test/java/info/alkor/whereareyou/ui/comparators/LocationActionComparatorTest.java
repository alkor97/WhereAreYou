package info.alkor.whereareyou.ui.comparators;

import org.junit.Test;

import java.util.Comparator;

import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationActionSide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Marlena on 2017-12-27.
 */
public class LocationActionComparatorTest {

    private final Comparator<LocationAction> cmp = LocationActionComparator.create();

    private LocationAction action(LocationActionSide side) {
        return new LocationAction(side);
    }

    @Test
    public void testComparison() {
        for (LocationActionSide.Type type : LocationActionSide.Type.values()) {
            LocationAction a = action(new LocationActionSide("123", "u123", type));
            LocationAction b = action(new LocationActionSide("123", "u123", type));
            assertEquals(0, cmp.compare(a, b));

            b.setState(LocationAction.State.QUERIED);
            assertTrue(cmp.compare(a, b) < 0);
            assertTrue(cmp.compare(b, a) > 0);

            a.setState(LocationAction.State.ANSWERED);
            assertTrue(cmp.compare(a, b) > 0);
            assertTrue(cmp.compare(b, a) < 0);
        }
    }
}
