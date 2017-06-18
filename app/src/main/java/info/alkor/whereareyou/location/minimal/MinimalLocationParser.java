package info.alkor.whereareyou.location.minimal;

import android.location.Location;
import android.support.annotation.NonNull;

import java.text.ParseException;

import info.alkor.whereareyou.location.LocationHelpers;
import info.alkor.whereareyou.location.LocationParser;

/**
 * Default location parser.
 * Created by Marlena on 2017-06-07.
 */
class MinimalLocationParser implements LocationParser {

    private final LocationHelpers h = new LocationHelpers();
    private final LocationFactory factory;

    public MinimalLocationParser() {
        this(new LocationFactory() {
            @Override
            public Location create(String provider) {
                return new Location(provider);
            }
        });
    }

    MinimalLocationParser(@NonNull LocationFactory factory) {
        this.factory = factory;
    }

    @NonNull
    @Override
    public Location parse(@NonNull String textLocation) {
        String[] entries = textLocation.split(",");

        final Location location = factory.create("");
        assign(location, entries, MinimalLocationFormat.Field.PROVIDER, new Assigner() {
            @Override
            public Location assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                location.setProvider(entries[field.getPosition()]);
                return location;
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.DATE, new Assigner() {
            @Override
            public Location assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                location.setTime(h.parseDate(entries[field.getPosition()]).getTime());
                return location;
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.LATITUDE, new Assigner() {
            @Override
            public Location assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                location.setLatitude(h.parseCoordinate(entries[field.getPosition()]));
                return location;
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.LONGITUDE, new Assigner() {
            @Override
            public Location assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                location.setLongitude(h.parseCoordinate(entries[field.getPosition()]));
                return location;
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.ALTITUDE, new Assigner() {
            @Override
            public Location assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                location.setAltitude(h.parseAltitude(entries[field.getPosition()]));
                return location;
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.ACCURACY, new Assigner() {
            @Override
            public Location assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                location.setAccuracy(h.parseAccuracy(entries[field.getPosition()]));
                return location;
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.BEARING, new Assigner() {
            @Override
            public Location assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                location.setBearing(h.parseBearing(entries[field.getPosition()]));
                return location;
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.SPEED, new Assigner() {
            @Override
            public Location assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                location.setSpeed(h.parseSpeed(entries[field.getPosition()]));
                return location;
            }
        });
        return location;
    }

    private Location assign(Location location, String[] entries, MinimalLocationFormat.Field field, Assigner assigner) {
        final int position = field.getPosition();
        if (entries.length > position) {
            final String entry = entries[position];
            try {
                return assigner.assign(location, entries, field);
            } catch (ParseException e) {
                throw new ParsingException("Error parsing " + field.name() + " '" + entry + "'", e);
            }
        }
        if (field.isMandatory()) {
            throw new ParsingException("Field " + field.name() + " not present in input");
        }
        return location;
    }

    interface LocationFactory {
        Location create(String provider);
    }

    private interface Assigner {
        Location assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException;
    }
}
