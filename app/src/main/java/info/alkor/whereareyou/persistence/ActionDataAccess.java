package info.alkor.whereareyou.persistence;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.android.ContactsHelper;
import info.alkor.whereareyou.common.TextHelper;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationActionSide;

/**
 * Action data access.
 * Created by Marlena on 2018-01-28.
 */
public class ActionDataAccess {

    private static final TextHelper TEXT_HELPER = new TextHelper();
    private final ActionDAO dao;
    private final ContactsHelper contactsHelper;

    public ActionDataAccess(WhereAreYouContext context) {
        dao = context.getDatabase().getActionsDAO();
        contactsHelper = context.getContactsHelper();
    }

    public LiveData<List<LocationAction>> getAllActionsLive() {
        return Transformations.map(dao.getAllActionsLive(), new Function<List<ActionEntity>, List<LocationAction>>() {
            @Override
            public List<LocationAction> apply(List<ActionEntity> input) {
                List<LocationAction> result = new ArrayList<>();
                for (ActionEntity entity : input) {
                    result.add(convert(entity));
                }
                return result;
            }
        });
    }

    public Provider<List<LocationAction>> getAllActions() {
        final AsyncTask<Void, Void, List<ActionEntity>> task = new GetAllActions(dao).execute();
        return new Provider<List<LocationAction>>() {
            @Override
            public List<LocationAction> get() {
                List<LocationAction> actions = new ArrayList<>();
                try {
                    for (ActionEntity entity : task.get()) {
                        actions.add(convert(entity));
                    }
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
                return actions;
            }
        };
    }

    private LocationAction convert(ActionEntity entity) {
        LocationAction action = new LocationAction(entity.id, convert(entity.phone, entity.type), entity.state);
        action.setLocation(convert(entity.location));
        action.setDeliveryStatus(entity.delivery);
        return action;
    }

    private LocationActionSide convert(String phone, LocationActionSide.Type type) {
        return new LocationActionSide(phone, contactsHelper.getDisplayName(phone), type);
    }

    private Location convert(LocationEntity entity) {
        if (entity != null) {
            Location location = new Location(entity.provider);
            location.setTime(entity.time);
            location.setLatitude(entity.latitude);
            location.setLongitude(entity.longitude);
            if (entity.altitude != null) {
                location.setAltitude(entity.altitude);
            }
            if (entity.accuracy != null) {
                location.setAccuracy(entity.accuracy);
            }
            if (entity.bearing != null) {
                location.setBearing(entity.bearing);
            }
            if (entity.speed != null) {
                location.setSpeed(entity.speed);
            }
            return location;
        }
        return null;
    }

    private ActionEntity convert(LocationAction action) {
        ActionEntity entity = new ActionEntity();
        entity.id = action.getActionId();
        entity.phone = TEXT_HELPER.normalizePhone(action.getSide().getPhone());
        entity.type = action.getSide().getType();

        entity.state = action.getState();
        entity.delivery = action.getDeliveryStatus();
        entity.location = convert(action.getLocation());

        return entity;
    }

    private LocationEntity convert(Location location) {
        if (location != null) {
            LocationEntity entity = new LocationEntity();
            entity.time = location.getTime();
            entity.provider = location.getProvider();

            entity.latitude = location.getLatitude();
            entity.longitude = location.getLongitude();

            if (location.hasAltitude()) {
                entity.altitude = location.getAltitude();
            }
            if (location.hasBearing()) {
                entity.bearing = location.getBearing();
            }
            if (location.hasAccuracy()) {
                entity.accuracy = location.getAccuracy();
            }
            if (location.hasSpeed()) {
                entity.speed = location.getSpeed();
            }
            return entity;
        }
        return null;
    }

    public Provider<LocationAction> addAction(LocationAction action) {
        return new UpdatedActionProvider<>(new AddActionTask(dao).execute(convert(action)));
    }

    public void deleteAction(LocationAction action) {
        new UpdatedActionProvider<>(new RemoveActionTask(dao).execute(action.getActionId()));
    }

    public Provider<LocationAction> setState(long actionId, LocationAction.State state) {
        return new UpdatedActionProvider<>(new SetStateTask(dao, state).execute(actionId));
    }

    public Provider<LocationAction> updateLocation(long actionId, Location location) {
        return new UpdatedActionProvider<>(new UpdateLocationTask(dao, convert(location)).execute(actionId));
    }

    public Provider<LocationAction> find(long actionId) {
        return new UpdatedActionProvider<>(new FindTask(dao).execute(actionId));
    }

    public Provider<LocationAction> findRelatedNotFulfilledAction(LocationActionSide provider) {
        return new UpdatedActionProvider<>(new FindRelatedNotFulfilledActionTask(dao).execute(provider.getPhone()));
    }

    public Provider<LocationAction> updateDeliveryStatus(long actionId, LocationAction.DeliveryStatus deliveryStatus) {
        return new UpdatedActionProvider<>(new UpdateDeliveryStatusTask(dao, deliveryStatus).execute(actionId));
    }

    public interface Provider<E> {
        E get();
    }

    private static class GetAllActions extends AsyncTask<Void, Void, List<ActionEntity>> {

        private final ActionDAO dao;

        GetAllActions(ActionDAO dao) {
            this.dao = dao;
        }

        @Override
        protected List<ActionEntity> doInBackground(Void... voids) {
            return dao.getAllActions();
        }
    }

    private static class UpdateLocationTask extends AsyncTask<Long, Void, ActionEntity> {

        private final ActionDAO dao;
        private final LocationEntity location;

        UpdateLocationTask(ActionDAO dao, LocationEntity location) {
            this.dao = dao;
            this.location = location;
        }

        @Override
        protected ActionEntity doInBackground(Long... longs) {
            dao.updateLocation(longs[0],
                    location.time,
                    location.provider,
                    location.latitude,
                    location.longitude,
                    location.altitude,
                    location.accuracy,
                    location.bearing,
                    location.speed);
            return dao.find(longs[0]).get(0);
        }
    }

    private static class SetStateTask extends AsyncTask<Long, Void, ActionEntity> {
        private final ActionDAO dao;
        private final LocationAction.State state;

        SetStateTask(ActionDAO dao, LocationAction.State state) {
            this.dao = dao;
            this.state = state;
        }

        @Override
        protected ActionEntity doInBackground(Long... longs) {
            dao.updateState(longs[0], state);
            Log.i("action " + longs[0], "state=" + state);
            return dao.find(longs[0]).get(0);
        }
    }

    private static class UpdateDeliveryStatusTask extends AsyncTask<Long, Void, ActionEntity> {

        private final ActionDAO dao;
        private final LocationAction.DeliveryStatus status;

        UpdateDeliveryStatusTask(ActionDAO dao, LocationAction.DeliveryStatus status) {
            this.dao = dao;
            this.status = status;
        }

        @Override
        protected ActionEntity doInBackground(Long... longs) {
            dao.updateDeliveryStatus(longs[0], status);
            Log.i("action " + longs[0], "delivery=" + status);
            return dao.find(longs[0]).get(0);
        }
    }

    private static class FindRelatedNotFulfilledActionTask extends AsyncTask<String, Void, ActionEntity> {

        private final ActionDAO dao;

        FindRelatedNotFulfilledActionTask(ActionDAO dao) {
            this.dao = dao;
        }

        @Override
        protected ActionEntity doInBackground(String... strings) {
            return dao.findRelatedNotFulfilledAction(LocationAction.State.QUERIED, strings[0]).get(0);
        }
    }

    private static class FindTask extends AsyncTask<Long, Void, ActionEntity> {

        private final ActionDAO dao;

        FindTask(ActionDAO dao) {
            this.dao = dao;
        }

        @Override
        protected ActionEntity doInBackground(Long... longs) {
            return dao.find(longs[0]).get(0);
        }
    }

    private static class AddActionTask extends AsyncTask<ActionEntity, Void, ActionEntity> {

        private final ActionDAO dao;

        AddActionTask(ActionDAO dao) {
            this.dao = dao;
        }

        @Override
        protected ActionEntity doInBackground(ActionEntity... entities) {
            long id = dao.addActions(entities)[0];
            Log.i("action " + id, "created");
            return dao.find(id).get(0);
        }
    }

    private static class RemoveActionTask extends AsyncTask<Long, Void, ActionEntity> {

        private final ActionDAO dao;

        RemoveActionTask(ActionDAO dao) {
            this.dao = dao;
        }

        @Override
        protected ActionEntity doInBackground(Long... ids) {
            dao.deleteAction(ids[0]);
            Log.i("action " + ids[0], "updated");
            return null;
        }
    }

    private class UpdatedActionProvider<Params> implements Provider<LocationAction> {

        private final AsyncTask<Params, Void, ActionEntity> task;

        UpdatedActionProvider(AsyncTask<Params, Void, ActionEntity> task) {
            this.task = task;
        }

        @Override
        public LocationAction get() {
            try {
                return convert(task.get());
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
