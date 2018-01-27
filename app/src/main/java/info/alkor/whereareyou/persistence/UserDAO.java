package info.alkor.whereareyou.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * User data access object.
 * Created by Marlena on 2018-01-25.
 */
@Dao
public interface UserDAO {

    @Query("SELECT * FROM " + UserEntity.TABLE_NAME)
    LiveData<List<UserEntity>> getAllUsers();

    @Insert(onConflict = IGNORE)
    void addUsers(UserEntity... users);

    @Delete
    void deleteUsers(UserEntity... users);
}
