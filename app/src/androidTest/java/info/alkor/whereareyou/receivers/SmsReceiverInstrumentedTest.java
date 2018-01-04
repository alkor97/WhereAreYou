package info.alkor.whereareyou.receivers;

import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.model.LocationAction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * SMS smsReceiver test.
 * Created by Marlena on 2018-01-02.
 */
@RunWith(AndroidJUnit4.class)
public class SmsReceiverInstrumentedTest {

    private final SmsReceiver smsReceiver = new SmsReceiver();
    private final LocationUpdateReceiver locationUpdateReceiver = new LocationUpdateReceiver();
    private WhereAreYouContext context;

    @Before
    public void beforeTest() {
        context = (WhereAreYouContext) InstrumentationRegistry.getTargetContext().getApplicationContext();
    }

    @Test
    public void handleLocationProviding() throws Exception {
        final String phone = "+48123456789";
        final String name = "Test1";
        smsReceiver.onReceive(context, phone, name, context.getLocationRequestCommand());

        assertEquals(1, context.getModel().size());
        LocationAction action = context.getModel().get(0);
        assertNotNull(action);
        assertEquals(LocationAction.State.QUERIED, action.getState());
        assertEquals(phone, action.getPhoneNumber());
        assertEquals(name, action.getDisplayName());
        assertEquals(LocationAction.DeliveryStatus.PENDING, action.getDeliveryStatus());

        final Location location = new Location("gps");
        locationUpdateReceiver.onReceive(context, action.getActionId(), location);

        assertEquals(1, context.getModel().size());
        action = context.getModel().find(action.getActionId());
        assertNotNull(action);
        assertEquals(location, action.getLocation());

        // wait slightly longer for another location update
        Thread.sleep(500 + context.getApplicationSettings().getLocationSettings()
                .getMaxAwaitTimeForBetterLocationAccuracy(TimeUnit.MILLISECONDS));

        assertEquals(1, context.getModel().size());
        action = context.getModel().find(action.getActionId());
        assertNotNull(action);

        assertEquals(LocationAction.State.ANSWERED, action.getState());
        assertEquals(LocationAction.DeliveryStatus.DELIVERED, action.getDeliveryStatus());
    }
}
