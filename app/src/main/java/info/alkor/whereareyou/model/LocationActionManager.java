package info.alkor.whereareyou.model;

import info.alkor.whereareyou.senders.LocationActionsSender;

/**
 * Created by Marlena on 2017-12-26.
 */

public class LocationActionManager {

    private final LocationActionList model;
    private final LocationActionsSender helper;

    public LocationActionManager(LocationActionList model, LocationActionsSender helper) {
        this.model = model;
        this.helper = helper;
    }

    public void addAction(LocationAction action) {
        final int position = model.addAction(action);
        if (helper != null) {
            if (position < 0) {
                helper.notifyActionAdded(position);
            } else {
                helper.notifyActionChanged(position);
            }
        }
    }

    public void changeAction(LocationAction action) {
        addAction(action);
    }

    public LocationAction findRelatedNotFulfilledAction(LocationActionSide side) {
        return model.findRelatedNotFulfilledAction(side);
    }
}
