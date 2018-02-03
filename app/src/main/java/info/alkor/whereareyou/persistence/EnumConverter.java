package info.alkor.whereareyou.persistence;

import android.arch.persistence.room.TypeConverter;

import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationActionSide;

/**
 * Enum converter.
 * Created by Marlena on 2018-01-30.
 */
abstract class EnumConverter<E extends Enum<E>> {

    private final Class<E> enumClass;

    EnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @TypeConverter
    public String convert(E e) {
        return e.name();
    }

    @TypeConverter
    public E convert(String name) {
        return Enum.valueOf(enumClass, name);
    }

    static class SideType extends EnumConverter<LocationActionSide.Type> {
        SideType() {
            super(LocationActionSide.Type.class);
        }
    }

    static class ActionState extends EnumConverter<LocationAction.State> {
        ActionState() {
            super(LocationAction.State.class);
        }
    }

    static class ActionDeliveryStatus extends EnumConverter<LocationAction.DeliveryStatus> {
        ActionDeliveryStatus() {
            super(LocationAction.DeliveryStatus.class);
        }
    }
}
