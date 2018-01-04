package info.alkor.whereareyou.model;

import info.alkor.whereareyou.senders.LocationActionsSender;

/**
 * Manages model operations and takes care of updating model views.
 * Created by Marlena on 2017-12-26.
 */
public class LocationActionManager {

    private final LocationActionList model;
    private final LocationActionsSender helper;

    public LocationActionManager(LocationActionList model, LocationActionsSender helper) {
        this.model = model;
        this.helper = helper;
    }

    void addAction(LocationAction action) {
        final int position = model.addAction(action);
        if (helper != null) {
            if (position < 0) {
                helper.notifyActionAdded(position);
            } else {
                helper.notifyActionChanged(position);
            }
        }
    }

    void changeAction(LocationAction action) {
        addAction(action);
    }

    public void removeAction(int idx) {
        final int position = model.removeAction(idx);
        if (position >= 0) {
            helper.notifyActionRemoved(position);
        }
    }

    LocationAction findRelatedNotFulfilledAction(LocationActionSide side) {
        return model.findRelatedNotFulfilledAction(side);
    }

    LocationAction find(long actionId) {
        return model.find(actionId);
    }
}
