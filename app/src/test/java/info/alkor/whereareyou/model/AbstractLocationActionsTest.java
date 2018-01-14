package info.alkor.whereareyou.model;

import org.junit.Test;

import java.util.Comparator;

import info.alkor.whereareyou.ui.comparators.LocationActionComparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Abstract location actions test.
 * Created by Marlena on 2018-01-14.
 */
public abstract class AbstractLocationActionsTest {

    private final Comparator<LocationAction> cmp = LocationActionComparator.create();
    private final LocationActions model;

    AbstractLocationActionsTest(LocationActions model) {
        this.model = model;
    }

    private LocationActionSide provider(String phone) {
        return LocationActionSide.provider(phone, "u" + phone);
    }

    private LocationActionSide requester(String phone) {
        return LocationActionSide.requester(phone, "u" + phone);
    }

    private LocationAction action(LocationActionSide side) throws Exception {
        try {
            return new LocationAction(side);
        } finally {
            Thread.sleep(1);
        }
    }

    private void assertContent(LocationAction... expected) {
        assertEquals(expected.length, model.size());
        for (int position = 0; position < model.size(); ++position) {
            assertEquals(0, cmp.compare(expected[position], model.get(position)));
        }
    }

    @Test
    public void testAdding() throws Exception {
        LocationAction request = action(provider("123"));
        LocationAction response = action(requester("123"));

        // new insertion
        assertEquals(-1, model.addAction(request));
        assertContent(request);

        // new insertion
        assertEquals(-1, model.addAction(response));
        assertContent(response, request);
    }

    @Test
    public void findRelatedNonFulfilledAction() throws Exception {
        LocationActionSide provider = provider("234");

        // uninitialized will be ignored
        LocationAction uninitialized = action(provider);
        model.addAction(uninitialized);

        // already answered will be ignored
        LocationAction answered = action(provider);
        answered.setState(LocationAction.State.ANSWERED);
        model.addAction(answered);

        // queried one will be returned
        LocationAction query = action(provider);
        query.setState(LocationAction.State.QUERIED);
        model.addAction(query);

        assertEquals(query, model.findRelatedNotFulfilledAction(provider));
    }

    @Test
    public void noRelatedNotFulfilledActionFound() throws Exception {
        LocationAction query = action(provider("123"));
        query.setState(LocationAction.State.QUERIED);
        model.addAction(query);

        // this will be ignored as there is mismatch in type
        assertNull(model.findRelatedNotFulfilledAction(requester("123")));

        // this will be ignored as there is mismatch in phone
        assertNull(model.findRelatedNotFulfilledAction(provider("1234")));

        // this will be ignored as there is mismatch in name
        assertNull(model.findRelatedNotFulfilledAction(LocationActionSide.provider("123", "dummy")));
    }

}
