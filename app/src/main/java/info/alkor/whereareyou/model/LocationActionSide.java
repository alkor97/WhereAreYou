package info.alkor.whereareyou.model;

import java.io.Serializable;

/**
 * Location action side.
 * Created by Marlena on 2017-11-06.
 */
public class LocationActionSide implements Serializable {

    private final String name;
    private final String phone;
    private final Type type;

    public LocationActionSide(String phone, String name, Type type) {
        this.name = name;
        this.phone = phone;
        this.type = type;
    }

    public static LocationActionSide requester(String phone, String name) {
        return new LocationActionSide(phone, name, Type.REQUESTER);
    }

    public static LocationActionSide provider(String phone, String name) {
        return new LocationActionSide(phone, name, Type.PROVIDER);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationActionSide that = (LocationActionSide) o;

        return (name != null ? name.equals(that.name) : that.name == null) && (phone != null ? phone.equals(that.phone) : that.phone == null) && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public enum Type {
        REQUESTER, PROVIDER
    }
}
