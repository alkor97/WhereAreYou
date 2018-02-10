package info.alkor.whereareyou.model;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.common.TextHelper;
import info.alkor.whereareyou.persistence.ActionDataAccess;
import info.alkor.whereareyou.senders.LocationRequestSender;
import info.alkor.whereareyou.senders.LocationResponseSender;

/**
 * Query flow manager.
 * Created by Marlena on 2017-12-29.
 */
public class LocationQueryFlowManager {

    private static final TextHelper TEXT_HELPER = new TextHelper();
    private final LocationRequestSender requestSender;
    private final LocationResponseSender responseSender;
    private final ActionDataAccess actionsAccess;

    /**
     * Instance constructor.
     *
     * @param context application context
     */
    public LocationQueryFlowManager(@NonNull WhereAreYouContext context) {
        this.requestSender = new LocationRequestSender(context);
        this.responseSender = new LocationResponseSender(context);
        this.actionsAccess = context.getActionDataAccess();
    }

    /**
     * Handle incoming location request.
     *
     * @param phone requester's phone number
     * @param name  requester's display name
     * @return related location action or null is similar request is already processed
     */
    public @Nullable
    LocationAction onIncomingLocationRequest(@NonNull String phone, @Nullable String name) {
        final LocationActionSide requester = LocationActionSide.requester(TEXT_HELPER
                .normalizePhone(phone), name);
        final LocationAction locationRequest = new LocationAction(0, requester, LocationAction
                .State.QUERIED);
        return actionsAccess.addAction(locationRequest).get();
    }

    /**
     * Handle incoming location response.
     *
     * @param phone    provider's phone number
     * @param name     provider's display name
     * @param location related location
     */
    public void onIncomingLocationResponse(@NonNull String phone, @Nullable String name, @NonNull
            Location location) {
        final LocationActionSide provider = LocationActionSide.provider(TEXT_HELPER
                .normalizePhone(phone), name);
        final LocationAction action = actionsAccess.findRelatedNotFulfilledAction(provider).get();
        if (action != null) {
            actionsAccess.setState(action.getActionId(), LocationAction.State.ANSWERED);
            actionsAccess.updateLocation(action.getActionId(), location);
        }
    }

    /**
     * Send location request.
     *
     * @param phone provider's phone number
     * @param name  provider's display name
     */
    public void sendLocationRequest(@NonNull String phone, @Nullable String name) {
        final LocationActionSide provider = LocationActionSide.provider(TEXT_HELPER
                .normalizePhone(phone), name);
        LocationAction action = actionsAccess.addAction(new LocationAction(0, provider,
                LocationAction.State.QUERIED)).get();
        requestSender.sendLocationRequest(action);
    }

    /**
     * Update location with more up-to-date data.
     *
     * @param actionId identifier of related action
     * @param location location to update with
     * @return non-null if location action matching provided action identifier exists
     */
    public @Nullable
    LocationAction updateLocation(long actionId, @NonNull Location location) {
        LocationAction action = actionsAccess.find(actionId).get();
        if (action != null
                && action.getState() != LocationAction.State.ANSWERED
                && (action.getLocation() == null
                || location.getAccuracy() < action.getLocation().getAccuracy()
                || newerByAtLeastOneMinute(action.getLocation(), location))) {
            action = actionsAccess.updateLocation(actionId, location).get();
        }
        return action;
    }

    private boolean newerByAtLeastOneMinute(@NonNull Location reference, @NonNull Location
            proposal) {
        return Math.abs(proposal.getTime() - reference.getTime()) > 60_000;
    }

    /**
     * Send location response.
     *
     * @param action related location action
     */
    public void sendLocationResponse(@NonNull LocationAction action) {
        if (action.getState() != LocationAction.State.ANSWERED) {
            action = actionsAccess.setState(action.getActionId(), LocationAction.State.ANSWERED)
                    .get();
            responseSender.sendLocationResponse(action);
        }
    }

    /**
     * Update SMS message delivery status.
     *
     * @param actionId identifier of related location action
     * @param status   SMS message delivery status
     */
    public void updateDeliveryStatus(long actionId, @NonNull LocationAction.DeliveryStatus status) {
        actionsAccess.updateDeliveryStatus(actionId, status);
    }
}
