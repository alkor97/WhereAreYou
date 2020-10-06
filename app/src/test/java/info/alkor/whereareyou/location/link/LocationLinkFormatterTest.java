package info.alkor.whereareyou.location.link;

import android.location.Location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import info.alkor.whereareyou.location.LocationHelpers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Location link formatting tests.
 * Created by Marlena on 2018-01-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class LocationLinkFormatterTest {

    private final LocationLinkFormatter formatter = new LocationLinkFormatter("http://alkor" +
            ".info/loc/?q=");
    private final LocationHelpers helpers = new LocationHelpers();
    private final Date now = new Date();
    private final String date = helpers.formatDate(now);
    @Mock
    private Location location;

    @Test
    public void testFullContent() {
        when(location.getProvider()).thenReturn("gps");
        when(location.getTime()).thenReturn(now.getTime());
        when(location.getLongitude()).thenReturn(14.3);
        when(location.getLatitude()).thenReturn(53.1);

        when(location.hasAltitude()).thenReturn(true);
        when(location.getAltitude()).thenReturn(56.0);

        when(location.hasAccuracy()).thenReturn(true);
        when(location.getAccuracy()).thenReturn(3.1f);

        when(location.hasBearing()).thenReturn(true);
        when(location.getBearing()).thenReturn(134.2f);

        when(location.hasSpeed()).thenReturn(true);
        when(location.getSpeed()).thenReturn(12.3f);

        final String expected = "http://loc.alkor.info/?q=" + date + ",gps,53.1,14.3,56,3,134,12.3";
        assertEquals(expected, formatter.format(location));
    }
}
