package info.alkor.whereareyou.location.link;

import android.location.Location;
import android.support.annotation.NonNull;

import info.alkor.whereareyou.location.LocationParser;
import info.alkor.whereareyou.location.minimal.MinimalLocationParser;

/**
 * Location link parser.
 * Created by Marlena on 2018-01-05.
 */
public class LocationLinkParser extends MinimalLocationParser {

    private final String smsLinkPrefix;

    public LocationLinkParser(@NonNull String smsLinkPrefix) {
        this.smsLinkPrefix = smsLinkPrefix;
    }

    LocationLinkParser(@NonNull String smsLinkPrefix, @NonNull LocationFactory factory) {
        super(factory);
        this.smsLinkPrefix = smsLinkPrefix;
    }

    @Override
    @NonNull
    public Location parse(@NonNull String text) throws LocationParser.ParsingException {
        if (text.startsWith(smsLinkPrefix)) {
            final String withoutPrefix = text.substring(smsLinkPrefix.length());
            return super.parse(withoutPrefix);
        }
        throw new LocationParser.ParsingException("Input does not start with expected prefix");
    }

    interface LocationFactory extends MinimalLocationParser.LocationFactory {
    }
}
