package info.alkor.whereareyou.location.link;

import android.location.Location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import info.alkor.whereareyou.location.LocationHelpers;
import info.alkor.whereareyou.location.LocationParser;

import static org.mockito.Mockito.verify;

/**
 * Location link parsing tests.
 * Created by Marlena on 2018-01-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class LocationLinkParserTest {

    private final LocationHelpers helpers = new LocationHelpers();
    private final Date now = new Date();
    private final String date = helpers.formatDate(now);
    @Mock
    private Location location;
    private final LocationParser parser = new LocationLinkParser("http://alkor.info/loc/?q=", new
            LocationLinkParser.LocationFactory() {
        @Override
        public Location create(String provider) {
            return location;
        }
    });

    private long getTime() {
        return (now.getTime() / 1000) * 1000;
    }

    @Test(expected = LocationParser.ParsingException.class)
    public void testMismatchedPrefix() throws LocationParser.ParsingException {
        parser.parse("https://aalkor.info/loca/?q=" + date + ",gps,53.1,14.3,15,2,93,13.1");
    }

    @Test
    public void testParseContentWithSpeed() throws LocationParser.ParsingException {
        parser.parse("http://alkor.info/loc/?q=" + date + ",gps,53.1,14.3,15,2,93,13.1");
        verify(location).setProvider("gps");
        verify(location).setTime(getTime());
        verify(location).setLongitude(14.3);
        verify(location).setLatitude(53.1);
        verify(location).setAltitude(15.0);
        verify(location).setAccuracy(2.0f);
        verify(location).setBearing(93.0f);
        verify(location).setSpeed(13.1f);
    }
}
