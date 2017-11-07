package info.alkor.whereareyou.ui;

/**
 * Created by Marlena on 2017-11-06.
 */

public class LocationActionSide {

    public static enum Type {
        REQUESTER, PROVIDER
    }

    private final String name;
    private final String phone;
    private final Type type;

    public LocationActionSide(String name, String phone, Type type) {
        this.name = name;
        this.phone = phone;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Type getType() {
        return type;
    }

    public static LocationActionSide requester(String name, String phone) {
        return new LocationActionSide(name, phone, Type.REQUESTER);
    }

    public static LocationActionSide provider(String name, String phone) {
        return new LocationActionSide(name, phone, Type.PROVIDER);
    }
}
