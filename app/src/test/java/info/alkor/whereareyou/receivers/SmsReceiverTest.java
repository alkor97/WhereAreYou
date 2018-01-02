package info.alkor.whereareyou.receivers;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.settings.ApplicationSettings;
import info.alkor.whereareyou.settings.LocationSettings;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * SmsReceiver tests.
 * Created by Marlena on 2018-01-01.
 */
@RunWith(MockitoJUnitRunner.class)
public class SmsReceiverTest {

    @Mock
    private WhereAreYouContext mockContext;

    @Mock
    private LocationQueryFlowManager mockFlowManager;

    @Mock
    private LocationAction mockLocationAction;

    @Mock
    private ApplicationSettings mockApplicationSettings;

    @Mock
    private LocationSettings mockLockationSettings;

    private SmsReceiver receiver = new SmsReceiver();

    @Before
    public void beforeTest() {
        when(mockContext.getLocationQueryFlowManager()).thenReturn(mockFlowManager);
    }

    @Test
    public void ignoreUnrelatedMessages() {
        when(mockContext.getLocationRequestCommand()).thenReturn("Hey, where are you?");

        receiver.onReceive(mockContext, "123", "abc", "random text");

        verify(mockFlowManager, never()).onIncomingLocationRequest(anyString(), anyString());
        verify(mockFlowManager, never()).onIncomingLocationResponse(anyString(), anyString(), any(Location.class));
        verify(mockContext, never()).requestSingleLocationUpdate(anyString(), any(LocationAction.class));
    }

    @Test
    public void handleIncomingLocationRequest() {
        final String command = "Hey, where are you?";
        final String phone = "123";
        final String name = "abc";
        final Set<String> providers = new HashSet<>(Arrays.asList("gps", "network"));

        when(mockContext.getLocationRequestCommand()).thenReturn(command);
        when(mockFlowManager.onIncomingLocationRequest(phone, name)).thenReturn(mockLocationAction);
        when(mockContext.getApplicationSettings()).thenReturn(mockApplicationSettings);
        when(mockApplicationSettings.getLocationSettings()).thenReturn(mockLockationSettings);
        when(mockLockationSettings.getLocationProviders()).thenReturn(providers);
        for (String provider : providers) {
            when(mockContext.requestSingleLocationUpdate(provider, mockLocationAction)).thenReturn(true);
        }

        receiver.onReceive(mockContext, phone, name, command);

        verify(mockFlowManager).onIncomingLocationRequest(phone, name);
        for (String provider : providers) {
            verify(mockContext).requestSingleLocationUpdate(provider, mockLocationAction);
        }
        verify(mockFlowManager, never()).onIncomingLocationResponse(anyString(), anyString(), any(Location.class));
    }

    @Test
    public void ignoreIncomingLocationRequestIfThereIsOneAlreadyInProgress() {
        final String command = "Hey, where are you?";
        final String phone = "123";
        final String name = "abc";

        when(mockContext.getLocationRequestCommand()).thenReturn(command);

        // null indicates that there is one request already processed
        when(mockFlowManager.onIncomingLocationRequest(phone, name)).thenReturn(null);

        receiver.onReceive(mockContext, phone, name, command);

        verify(mockFlowManager, only()).onIncomingLocationRequest(phone, name);
        verify(mockFlowManager, never()).onIncomingLocationResponse(anyString(), anyString(), any(Location.class));
        verify(mockContext, never()).requestSingleLocationUpdate(anyString(), any(LocationAction.class));
    }
}
