package info.alkor.whereareyou.persistence;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationActionSide;

/**
 * Action entity.
 * Created by Marlena on 2018-01-28.
 */
@Entity(tableName = "actions")
public class ActionEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String phone = "";

    @TypeConverters({EnumConverter.SideType.class})
    @NonNull
    public LocationActionSide.Type type = LocationActionSide.Type.PROVIDER;

    @TypeConverters({EnumConverter.ActionState.class})
    @NonNull
    public LocationAction.State state = LocationAction.State.UNINITIALIZED;

    @TypeConverters({EnumConverter.ActionDeliveryStatus.class})
    @NonNull
    public LocationAction.DeliveryStatus delivery = LocationAction.DeliveryStatus.PENDING;

    @Embedded
    public LocationEntity location;
}
