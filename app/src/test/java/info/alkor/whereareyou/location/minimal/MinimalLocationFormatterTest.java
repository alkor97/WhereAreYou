package info.alkor.whereareyou.location.minimal;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import info.alkor.whereareyou.location.LocationFormatter;
import info.alkor.whereareyou.location.LocationHelpers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test of location formatter.
 * Created by Marlena on 2017-06-07.
 */
@RunWith(MockitoJUnitRunner.class)
public class MinimalLocationFormatterTest {

    private final LocationFormatter formatter = new MinimalLocationFormatter();
    private final LocationHelpers helpers = new LocationHelpers();
    private final Date now = new Date();
    private final String date = helpers.formatDate(now);
    @Mock
    private Location location;

    @Before
    public void before() {
        when(location.getProvider()).thenReturn("gps");
        when(location.getTime()).thenReturn(now.getTime());
        when(location.getLongitude()).thenReturn(14.3);
        when(location.getLatitude()).thenReturn(53.1);
    }

    @Test
    public void testMinimalContent() {
        when(location.hasAltitude()).thenReturn(false);
        when(location.hasAccuracy()).thenReturn(false);
        when(location.hasBearing()).thenReturn(false);
        when(location.hasSpeed()).thenReturn(false);

        final String expected = date + ",gps,53.1,14.3,,,,";
        assertEquals(expected, formatter.format(location));
    }

    @Test
    public void testFullContent() {
        when(location.hasAltitude()).thenReturn(true);
        when(location.getAltitude()).thenReturn(56.0);

        when(location.hasAccuracy()).thenReturn(true);
        when(location.getAccuracy()).thenReturn(3.1f);

        when(location.hasBearing()).thenReturn(true);
        when(location.getBearing()).thenReturn(134.2f);

        when(location.hasSpeed()).thenReturn(true);
        when(location.getSpeed()).thenReturn(12.3f);

        final String expected = date + ",gps,53.1,14.3,56,3,134,12.3";
        assertEquals(expected, formatter.format(location));
    }
}
