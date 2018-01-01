package info.alkor.whereareyou.model;

import android.content.Context;
import android.location.Location;

import info.alkor.whereareyou.WhereAreYou;
import info.alkor.whereareyou.common.TextHelper;
import info.alkor.whereareyou.senders.LocationRequestSender;
import info.alkor.whereareyou.senders.LocationResponseSender;

/**
 * Created by Marlena on 2017-12-29.
 */

public class LocationQueryFlowManager {

    private static final TextHelper TEXT_HELPER = new TextHelper();
    private final LocationActionManager modelManager;
    private final LocationRequestSender requestSender;
    private final LocationResponseSender responseSender;

    public LocationQueryFlowManager(Context context) {
        this.modelManager = ((WhereAreYou) context.getApplicationContext()).getModelManager();
        this.requestSender = new LocationRequestSender(context);
        this.responseSender = new LocationResponseSender(context);
    }

    public LocationAction onIncomingLocationRequest(String phone, String name) {
        final LocationActionSide requester = LocationActionSide.requester(TEXT_HELPER.normalizePhone(phone), name);
        final LocationAction action = modelManager.findRelatedNotFulfilledAction(requester);
        if (action == null) {
            final LocationAction locationRequest = new LocationAction(requester);
            locationRequest.setState(LocationAction.State.QUERIED);
            modelManager.addAction(locationRequest);
            return locationRequest;
        }
        return null;
    }

    public void onIncomingLocationResponse(String phone, String name, Location location) {
        final LocationActionSide provider = LocationActionSide.provider(TEXT_HELPER.normalizePhone(phone), name);
        final LocationAction action = modelManager.findRelatedNotFulfilledAction(provider);
        if (action != null) {
            action.setLocation(location);
            action.setState(LocationAction.State.ANSWERED);
            modelManager.changeAction(action);
        }
    }

    public void sendLocationRequest(String phone, String name) {
        final LocationActionSide provider = LocationActionSide.provider(TEXT_HELPER.normalizePhone(phone), name);
        final LocationAction action = new LocationAction(provider);
        action.setState(LocationAction.State.QUERIED);
        modelManager.addAction(action);
        requestSender.sendLocationRequest(action);
    }

    public LocationAction updateLocation(long actionId, Location location) {
        final LocationAction action = modelManager.find(actionId);
        if (action != null && (action.getLocation() == null
                || location.getAccuracy() < action.getLocation().getAccuracy()
                || newerByAtLeastOneMinute(action.getLocation(), location))) {
            action.setLocation(location);
            modelManager.changeAction(action);
        }
        return action;
    }

    private boolean newerByAtLeastOneMinute(Location reference, Location proposal) {
        return Math.abs(proposal.getTime() - reference.getTime()) > 60_000;
    }

    public void sendLocationResponse(LocationAction action) {
        if (action.getState() != LocationAction.State.ANSWERED) {
            action.setState(LocationAction.State.ANSWERED);
            modelManager.changeAction(action);

            responseSender.sendLocationResponse(action);
        }
    }

    public void updateDeliveryStatus(long actionId, LocationAction.DeliveryStatus status) {
        final LocationAction action = modelManager.find(actionId);
        if (action != null) {
            action.setDeliveryStatus(status);
            modelManager.changeAction(action);
        }
    }
}
