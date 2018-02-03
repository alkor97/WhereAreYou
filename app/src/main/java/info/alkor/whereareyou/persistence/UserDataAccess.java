package info.alkor.whereareyou.persistence;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.android.ContactsHelper;
import info.alkor.whereareyou.common.TextHelper;
import info.alkor.whereareyou.model.LocationActionSide;

/**
 * User data access.
 * Created by Marlena on 2018-01-25.
 */
public class UserDataAccess {

    private static final TextHelper TEXT_HELPER = new TextHelper();
    private final UserDAO dao;
    private final ContactsHelper contactsHelper;

    public UserDataAccess(WhereAreYouContext context) {
        dao = context.getDatabase().getUserDAO();
        contactsHelper = context.getContactsHelper();
    }

    public LiveData<List<LocationActionSide>> getAllUsers() {
        return Transformations.map(dao.getAllUsers(),
                new Function<List<UserEntity>, List<LocationActionSide>>() {
                    @Override
                    public List<LocationActionSide> apply(List<UserEntity> input) {
                        List<LocationActionSide> result = new ArrayList<>();
                        for (UserEntity user : input) {
                            result.add(LocationActionSide.provider(
                                    user.phone,
                                    contactsHelper.getDisplayName(user.phone)));
                        }
                        return result;
                    }
                });
    }

    public void addUser(LocationActionSide user) {
        UserEntity entity = new UserEntity();
        entity.phone = TEXT_HELPER.normalizePhone(user.getPhone());
        new AddUserTask(dao).execute(entity);
    }

    public void removeUser(LocationActionSide user) {
        UserEntity entity = new UserEntity();
        entity.phone = user.getPhone();
        new RemoveUserTask(dao).execute(entity);
    }

    private static class AddUserTask extends AsyncTask<UserEntity, Void, Void> {

        private final UserDAO dao;

        AddUserTask(UserDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            dao.addUsers(userEntities);
            return null;
        }
    }

    private static class RemoveUserTask extends AsyncTask<UserEntity, Void, Void> {

        private final UserDAO dao;

        RemoveUserTask(UserDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            dao.deleteUsers(userEntities);
            return null;
        }
    }
}
