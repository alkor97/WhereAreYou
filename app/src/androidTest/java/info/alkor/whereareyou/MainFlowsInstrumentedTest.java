package info.alkor.whereareyou;

import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.receivers.LocationUpdateReceiver;
import info.alkor.whereareyou.receivers.SmsReceiver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * SMS smsReceiver test.
 * Created by Marlena on 2018-01-02.
 */
@RunWith(AndroidJUnit4.class)
public class MainFlowsInstrumentedTest {

    private final SmsReceiver smsReceiver = new SmsReceiver();
    private final LocationUpdateReceiver locationUpdateReceiver = new LocationUpdateReceiver();
    private WhereAreYouContext context;

    @Before
    public void beforeTest() {
        context = (WhereAreYouContext) InstrumentationRegistry.getTargetContext().getApplicationContext();
    }

    @After
    public void afterTest() {
        while (context.getModel().size() > 0) {
            context.getModelManager().removeAction(0);
        }
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

    @Test
    public void handleLocationRequesting() throws Exception {
        final String phone = "+48987654321";
        final String name = "Test2";

        context.getLocationQueryFlowManager().sendLocationRequest(phone, name);
        assertEquals(1, context.getModel().size());
        LocationAction action = context.getModel().get(0);
        assertNotNull(action);
        assertEquals(LocationAction.State.QUERIED, action.getState());
        assertEquals(phone, action.getPhoneNumber());
        assertEquals(name, action.getDisplayName());
        assertEquals(LocationAction.DeliveryStatus.PENDING, action.getDeliveryStatus());

        Thread.sleep(1000);
        assertEquals(LocationAction.DeliveryStatus.DELIVERED, action.getDeliveryStatus());

        smsReceiver.onReceive(context, phone, name, "20180104064313,gps,53.438412,14.574477,157,65,3,1");
        assertEquals(1, context.getModel().size());
        action = context.getModel().get(0);
        assertNotNull(action);
        assertEquals(LocationAction.State.ANSWERED, action.getState());
    }
}
