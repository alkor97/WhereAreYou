package info.alkor.whereareyou.logic;

import java.net.URLEncoder;

import info.alkor.whereareyou.batery.BatteryLevelFormatter;
import info.alkor.whereareyou.location.minimal.MinimalLocationFormatter;

/**
 * Location response encoder.
 * Created by Marlena on 2017-04-01.
 */
public class LocationResponseEncoder {

    private final BatteryLevelFormatter batteryLevelFormatter = new BatteryLevelFormatter();
    private final MinimalLocationFormatter formatter3 = new MinimalLocationFormatter();

    public String encodeAlkorInfoLocationResponse(LocationResponse locationResponse) {
        return String.format("http://loc.alkor.info/?q=%s,%s,%s",
                formatter3.format(locationResponse.getLocation()),
                URLEncoder.encode(locationResponse.getDestinationAddress().startsWith("+")
                        ? ("00" + locationResponse.getDestinationAddress().substring(1))
                        : locationResponse.getDestinationAddress()),
                URLEncoder.encode(locationResponse.getDestinationName()));
    }
}
