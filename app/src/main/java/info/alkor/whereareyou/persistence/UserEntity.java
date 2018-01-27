package info.alkor.whereareyou.persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * User entity.
 * Created by Marlena on 2018-01-25.
 */
@Entity(tableName = UserEntity.TABLE_NAME)
public class UserEntity {

    static final String TABLE_NAME = "users";

    @PrimaryKey
    @NonNull
    public String phone = "";
}
