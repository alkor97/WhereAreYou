package info.alkor.whereareyou.comparators;

import org.junit.Test;

import java.util.Comparator;

import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationActionSide;

import static org.junit.Assert.assertTrue;

/**
 * Location action comparator tests.
 * Created by Marlena on 2017-12-27.
 */
public class LocationActionComparatorTest {

    private final Comparator<LocationAction> cmp = LocationActionComparator.create();

    private LocationAction action() {
        return new LocationAction(LocationActionSide.provider("a", "1"));
    }

    @Test
    public void testComparison() throws Exception {
        LocationAction action1 = action();
        Thread.sleep(1);
        LocationAction action2 = action();

        assertTrue(cmp.compare(action1, action2) > 0);
    }
}
