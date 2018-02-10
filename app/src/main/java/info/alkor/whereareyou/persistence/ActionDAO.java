package info.alkor.whereareyou.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import info.alkor.whereareyou.model.LocationAction;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Action data access object.
 * Created by Marlena on 2018-01-28.
 */
@Dao
public interface ActionDAO {

    @Query("SELECT * FROM actions ORDER BY id DESC")
    LiveData<List<ActionEntity>> getAllActionsLive();

    @Query("SELECT * FROM actions ORDER BY id")
    List<ActionEntity> getAllActions();

    @Insert(onConflict = REPLACE)
    long[] addActions(ActionEntity... actions);

    @Query("DELETE FROM actions WHERE id = :id")
    void deleteAction(long id);

    @Query("SELECT * FROM actions WHERE id = :id")
    List<ActionEntity> find(long id);

    @TypeConverters({EnumConverter.ActionState.class})
    @Query("SELECT * FROM actions WHERE state = :state AND phone = :phone")
    List<ActionEntity> findRelatedNotFulfilledAction(LocationAction.State state, String phone);

    @TypeConverters({EnumConverter.ActionDeliveryStatus.class})
    @Query("UPDATE actions SET delivery = :delivery WHERE id = :id")
    void updateDeliveryStatus(long id, LocationAction.DeliveryStatus delivery);

    @TypeConverters({EnumConverter.ActionState.class})
    @Query("UPDATE actions SET state = :state WHERE id = :id")
    void updateState(long id, LocationAction.State state);

    @Query("UPDATE actions SET time = :time, provider = :provider, latitude = :latitude, " +
            "longitude = :longitude, altitude = :altitude, accuracy = :accuracy, bearing = " +
            ":bearing, speed = :speed WHERE id = :id")
    void updateLocation(long id, long time, String provider, double latitude, double longitude,
                        Double altitude, Float accuracy, Float bearing, Float speed);
}
