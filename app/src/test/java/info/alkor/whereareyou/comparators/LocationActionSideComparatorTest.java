package info.alkor.whereareyou.comparators;

import org.junit.Test;

import java.util.Comparator;

import info.alkor.whereareyou.model.LocationActionSide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Location action side comparator test.
 * Created by Marlena on 2017-12-27.
 */
public class LocationActionSideComparatorTest {

    private final Comparator<LocationActionSide> cmp = LocationActionSideComparator.create();

    @Test
    public void compare() {
        LocationActionSide a = LocationActionSide.provider("123", "u" + 123);
        {
            LocationActionSide b = LocationActionSide.provider(a.getPhone(), a.getName());
            assertEquals(0, cmp.compare(a, b));
            assertEquals(0, cmp.compare(b, a));
        }
        {
            LocationActionSide b = LocationActionSide.provider("023", a.getName());
            assertTrue(cmp.compare(a, b) > 0);
            assertTrue(cmp.compare(b, a) < 0);
        }
        {
            LocationActionSide b = LocationActionSide.provider(a.getPhone(), "u0" + 23);
            assertTrue(cmp.compare(a, b) > 0);
            assertTrue(cmp.compare(b, a) < 0);
        }
        {
            LocationActionSide b = LocationActionSide.requester(a.getPhone(), a.getName());
            assertTrue(cmp.compare(a, b) > 0);
            assertTrue(cmp.compare(b, a) < 0);
        }
    }
}