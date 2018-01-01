package info.alkor.whereareyou.receivers;

import android.location.Location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.model.LocationQueryFlowManager;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * SmsReceiver tests.
 * Created by Marlena on 2018-01-01.
 */
@RunWith(MockitoJUnitRunner.class)
public class SmsReceiverTest {

    @Mock
    private WhereAreYouContext context;

    @Mock
    private LocationQueryFlowManager flowManager;

    private SmsReceiver receiver = new SmsReceiver();

    @Test
    public void ignoreUnrelatedMessages() {
        when(context.getLocationQueryFlowManager()).thenReturn(flowManager);
        when(context.getLocationRequestCommand()).thenReturn("Hey, where are you?");

        receiver.onReceive(context, "123", "abc", "random text");

        verify(flowManager, never()).onIncomingLocationRequest(anyString(), anyString());
        verify(flowManager, never()).onIncomingLocationResponse(anyString(), anyString(), any(Location.class));
    }
}
