package info.alkor.whereareyou;

import android.arch.persistence.room.Room;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.persistence.ActionDataAccess;
import info.alkor.whereareyou.persistence.AppDatabase;
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
    private AppDatabase db;
    private ActionDataAccess access;

    @Before
    public void beforeTest() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(), AppDatabase
                .class).build();
        context = (WhereAreYouContext) InstrumentationRegistry.getTargetContext()
                .getApplicationContext();
        access = context.getActionDataAccess();
        clearDatabase();

        context.getApplicationSettings().getLocationSettings()
                .setMaxAwaitTimeForBetterLocationAccuracy(1, TimeUnit.SECONDS);
    }

    @After
    public void afterTest() {
        //clearDatabase();
        db.close();
    }

    private void clearDatabase() {
        for (LocationAction action : access.getAllActions().get()) {
            access.deleteAction(action);
        }
    }

    private int actionsSize() {
        return access.getAllActions().get().size();
    }

    private LocationAction action() {
        return access.getAllActions().get().get(0);
    }

    @Test
    public void handleLocationProviding() throws Exception {
        final String phone = "+48123456789";
        final String name = "Test1";
        smsReceiver.onReceive(context, phone, name, context.getLocationRequestCommand());

        assertEquals(1, actionsSize());
        LocationAction action = action();
        assertNotNull(action);
        assertEquals(LocationAction.State.QUERIED, action.getState());
        assertEquals(phone, action.getPhoneNumber());
        assertEquals(name, action.getDisplayName());
        assertEquals(LocationAction.DeliveryStatus.PENDING, action.getDeliveryStatus());

        updateLocation(action.getActionId(), location());
    }

    @Test
    public void handleLocationRequesting() throws Exception {
        final String phone = "+48987654321";
        final String name = "Test2";
        final String pathPrefix = context.getSmsLinkPrefix();

        context.getLocationQueryFlowManager().sendLocationRequest(phone, name);
        assertEquals(1, actionsSize());
        LocationAction action = action();
        assertNotNull(action);
        assertEquals(LocationAction.State.QUERIED, action.getState());
        assertEquals(phone, action.getPhoneNumber());
        assertEquals(LocationAction.DeliveryStatus.PENDING, action.getDeliveryStatus());

        Thread.sleep(1000);
        action = action();
        assertEquals(LocationAction.DeliveryStatus.DELIVERED, action.getDeliveryStatus());

        smsReceiver.onReceive(context, phone, name, pathPrefix + "20180104064313,gps,53.438412," +
                "14.574477,157,65,3,1");
        assertEquals(1, actionsSize());
        action = action();
        assertNotNull(action);
        assertEquals(LocationAction.State.ANSWERED, action.getState());
    }

    private void checkLocationsEquality(Location a, Location b) {
        assertEquals(a.getTime(), b.getTime());
        assertEquals(a.getProvider(), b.getProvider());
        assertEquals(a.getLatitude(), b.getLatitude(), 0.00001);
        assertEquals(a.getLongitude(), b.getLongitude(), 0.00001);
        assertEquals(a.hasAltitude(), b.hasAltitude());
        assertEquals(a.getAltitude(), b.getAltitude(), 0.01);
        assertEquals(a.hasAccuracy(), b.hasAccuracy());
        assertEquals(a.getAccuracy(), b.getAccuracy(), 0.01f);
        assertEquals(a.hasBearing(), b.hasBearing());
        assertEquals(a.getBearing(), b.getBearing(), 1.0f);
        assertEquals(a.hasSpeed(), b.hasSpeed());
        assertEquals(a.getSpeed(), b.getSpeed(), 1.0f);
    }

    private void updateLocation(long actionId, Location location) throws Exception {
        locationUpdateReceiver.onReceive(context, actionId, location);

        assertEquals(1, actionsSize());
        for (LocationAction action : access.find(actionId).get()) {
            checkLocationsEquality(location, action.getLocation());
        }

        // wait slightly longer for another location update
        Thread.sleep(500 + context.getApplicationSettings().getLocationSettings()
                .getMaxAwaitTimeForBetterLocationAccuracy(TimeUnit.MILLISECONDS));

        assertEquals(1, actionsSize());
        for (LocationAction action : access.find(actionId).get()) {
            assertEquals(LocationAction.State.ANSWERED, action.getState());
            assertEquals(LocationAction.DeliveryStatus.DELIVERED, action.getDeliveryStatus());
        }
    }

    private Location location() {
        Location location = new Location("network");
        location.setTime(System.currentTimeMillis());
        location.setLatitude(53.0);
        location.setLongitude(14.0);
        location.setAltitude(57.0f);
        location.setAccuracy(13.0f);
        location.setBearing(12.0f);
        location.setSpeed(34.0f);
        return location;
    }
}
