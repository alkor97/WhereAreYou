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
public class MinimalLocationParser implements LocationParser {

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
    public Location parse(@NonNull String textLocation) throws ParsingException {
        String[] entries = textLocation.split(",");

        final Location location = factory.create("");
        assign(location, entries, MinimalLocationFormat.Field.PROVIDER, new Assigner() {
            @Override
            public void assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                location.setProvider(entries[field.getPosition()]);
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.DATE, new Assigner() {
            @Override
            public void assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                location.setTime(h.parseDate(entries[field.getPosition()]).getTime());
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.LATITUDE, new Assigner() {
            @Override
            public void assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                location.setLatitude(h.parseCoordinate(entries[field.getPosition()]));
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.LONGITUDE, new Assigner() {
            @Override
            public void assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                location.setLongitude(h.parseCoordinate(entries[field.getPosition()]));
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.ALTITUDE, new Assigner() {
            @Override
            public void assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                try {
                    location.setAltitude(h.parseAltitude(entries[field.getPosition()]));
                } catch (ParseException e) {
                    location.removeAltitude();
                }
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.ACCURACY, new Assigner() {
            @Override
            public void assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                try {
                    location.setAccuracy(h.parseAccuracy(entries[field.getPosition()]));
                } catch (ParseException e) {
                    location.removeAccuracy();
                }
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.BEARING, new Assigner() {
            @Override
            public void assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                try {
                    location.setBearing(h.parseBearing(entries[field.getPosition()]));
                } catch (ParseException e) {
                    location.removeBearing();
                }
            }
        });
        assign(location, entries, MinimalLocationFormat.Field.SPEED, new Assigner() {
            @Override
            public void assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException {
                try {
                    location.setSpeed(h.parseSpeed(entries[field.getPosition()]));
                } catch (ParseException e) {
                    location.removeSpeed();
                }
            }
        });
        return location;
    }

    private void assign(Location location, String[] entries, MinimalLocationFormat.Field field, Assigner assigner) throws ParsingException {
        final int position = field.getPosition();
        if (entries.length > position) {
            final String entry = entries[position];
            try {
                assigner.assign(location, entries, field);
            } catch (ParseException e) {
                throw new ParsingException("Error parsing " + field.name() + " '" + entry + "'", e);
            }
        }
        if (field.isMandatory()) {
            throw new ParsingException("Field " + field.name() + " not present in input");
        }
    }

    interface LocationFactory {
        Location create(String provider);
    }

    private interface Assigner {
        void assign(Location location, String[] entries, MinimalLocationFormat.Field field) throws ParseException;
    }
}
