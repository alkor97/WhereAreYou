package info.alkor.whereareyou.receivers;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.location.LocationParser;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationQueryFlowManager;
import info.alkor.whereareyou.settings.ApplicationSettings;
import info.alkor.whereareyou.settings.LocationSettings;

import static org.junit.Assert.assertEquals;
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
    private LocationAction mockLocationAction2;

    @Mock
    private ApplicationSettings mockApplicationSettings;

    @Mock
    private LocationSettings mockLocationSettings;

    @Mock
    private Location mockLocation;

    @Mock
    private LocationParser mockLocationParser;

    private SmsReceiver receiver = new SmsReceiver();

    @Before
    public void beforeTest() {
        when(mockContext.getLocationQueryFlowManager()).thenReturn(mockFlowManager);
    }

    @Test
    public void ignoreUnrelatedMessages() throws LocationParser.ParsingException {
        String message = "random text";

        when(mockContext.getLocationRequestCommand()).thenReturn("Hey, where are you?");
        when(mockContext.getLocationParser()).thenReturn(mockLocationParser);
        when(mockLocationParser.parse(message)).thenThrow(new LocationParser.ParsingException
                ("error"));

        receiver.onReceive(mockContext, "123", "abc", message);

        verify(mockFlowManager, never()).onIncomingLocationRequest(anyString(), anyString());
        verify(mockFlowManager, never()).onIncomingLocationResponse(anyString(), anyString(), any
                (Location.class));
        verify(mockContext, never()).requestSingleLocationUpdate(anyString(), any(LocationAction
                .class));
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
        when(mockApplicationSettings.getLocationSettings()).thenReturn(mockLocationSettings);
        when(mockLocationSettings.getLocationProviders()).thenReturn(providers);
        for (String provider : providers) {
            when(mockContext.requestSingleLocationUpdate(provider, mockLocationAction))
                    .thenReturn(true);
        }

        receiver.onReceive(mockContext, phone, name, command);

        verify(mockFlowManager).onIncomingLocationRequest(phone, name);
        for (String provider : providers) {
            verify(mockContext).requestSingleLocationUpdate(provider, mockLocationAction);
        }
        verify(mockFlowManager, never()).onIncomingLocationResponse(anyString(), anyString(), any
                (Location.class));
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
        verify(mockFlowManager, never()).onIncomingLocationResponse(anyString(), anyString(), any
                (Location.class));
        verify(mockContext, never()).requestSingleLocationUpdate(anyString(), any(LocationAction
                .class));
    }

    @Test
    public void handleParallelIncomingLocationRequests() {
        final String command = "Hey, where are you?";
        final String[] phones = {"123", "456"};
        final String[] names = {"abc", "def"};
        final LocationAction[] actions = {mockLocationAction, mockLocationAction2};
        assertEquals(phones.length, names.length);
        assertEquals(phones.length, actions.length);

        final Set<String> providers = new HashSet<>(Arrays.asList("gps", "network"));

        when(mockContext.getLocationRequestCommand()).thenReturn(command);
        for (int i = 0; i < phones.length; ++i) {
            when(mockFlowManager.onIncomingLocationRequest(phones[i], names[i])).thenReturn
                    (actions[i]);
        }
        when(mockContext.getApplicationSettings()).thenReturn(mockApplicationSettings);
        when(mockApplicationSettings.getLocationSettings()).thenReturn(mockLocationSettings);
        when(mockLocationSettings.getLocationProviders()).thenReturn(providers);
        for (int i = 0; i < phones.length; ++i) {
            for (String provider : providers) {
                when(mockContext.requestSingleLocationUpdate(provider, actions[i])).thenReturn
                        (true);
            }
        }

        for (int i = 0; i < phones.length; ++i) {
            receiver.onReceive(mockContext, phones[i], names[i], command);
        }

        for (int i = 0; i < phones.length; ++i) {
            verify(mockFlowManager).onIncomingLocationRequest(phones[i], names[i]);
            for (String provider : providers) {
                verify(mockContext).requestSingleLocationUpdate(provider, actions[i]);
            }
        }
        verify(mockFlowManager, never()).onIncomingLocationResponse(anyString(), anyString(), any
                (Location.class));
    }

    @Test
    public void handleIncomingLocationResponse() throws LocationParser.ParsingException {
        final String command = "20171231123456,gps,53.1,14.3,15,2,93,13.1";
        final String phone = "123";
        final String name = "abc";

        when(mockContext.getLocationParser()).thenReturn(mockLocationParser);
        when(mockLocationParser.parse(command)).thenReturn(mockLocation);

        receiver.onReceive(mockContext, phone, name, command);

        verify(mockFlowManager).onIncomingLocationResponse(phone, name, mockLocation);
        verify(mockFlowManager, never()).onIncomingLocationRequest(anyString(), anyString());
    }
}
