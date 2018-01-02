package info.alkor.whereareyou.location.minimal;

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
 * Tests of location parser.
 * Created by Marlena on 2017-06-07.
 */
@RunWith(MockitoJUnitRunner.class)
public class MinimalLocationParserTest {

    private final LocationHelpers helpers = new LocationHelpers();
    private final Date now = new Date();
    private final String date = helpers.formatDate(now);
    @Mock
    private Location location;
    private final LocationParser parser = new MinimalLocationParser(new MinimalLocationParser.LocationFactory() {
        @Override
        public Location create(String provider) {
            return location;
        }
    });

    private long getTime() {
        return (now.getTime() / 1000) * 1000;
    }

    @Test(expected = LocationParser.ParsingException.class)
    public void testParseEmptyString() throws LocationParser.ParsingException {
        parser.parse("");
    }

    @Test(expected = LocationParser.ParsingException.class)
    public void testParseWithoutCoordinates() throws LocationParser.ParsingException {
        parser.parse("gps");
    }

    @Test
    public void testParseMinimal() throws LocationParser.ParsingException {
        parser.parse(date + ",gps,53.1,14.3,,,,");
        verify(location).setProvider("gps");
        verify(location).setTime(getTime());
        verify(location).setLongitude(14.3);
        verify(location).setLatitude(53.1);
    }

    @Test
    public void testParseContentWithAltitude() throws LocationParser.ParsingException {
        parser.parse(date + ",gps,53.1,14.3,15,,,");
        verify(location).setProvider("gps");
        verify(location).setTime(getTime());
        verify(location).setLongitude(14.3);
        verify(location).setLatitude(53.1);
        verify(location).setAltitude(15.0);
    }

    @Test
    public void testParseContentWithAccuracy() throws LocationParser.ParsingException {
        parser.parse(date + ",gps,53.1,14.3,15,2,,");
        verify(location).setProvider("gps");
        verify(location).setTime(getTime());
        verify(location).setLongitude(14.3);
        verify(location).setLatitude(53.1);
        verify(location).setAltitude(15.0);
        verify(location).setAccuracy(2.0f);
    }

    @Test
    public void testParseContentWithBearing() throws LocationParser.ParsingException {
        parser.parse(date + ",gps,53.1,14.3,15,2,93,");
        verify(location).setProvider("gps");
        verify(location).setTime(getTime());
        verify(location).setLongitude(14.3);
        verify(location).setLatitude(53.1);
        verify(location).setAltitude(15.0);
        verify(location).setAccuracy(2.0f);
        verify(location).setBearing(93.0f);
    }

    @Test
    public void testParseContentWithSpeed() throws LocationParser.ParsingException {
        parser.parse(date + ",gps,53.1,14.3,15,2,93,13.1");
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
