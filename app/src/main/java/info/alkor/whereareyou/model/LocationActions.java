package info.alkor.whereareyou.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Interfaec of accessing location actions.
 * Created by Marlena on 2018-01-13.
 */
public interface LocationActions {
    /**
     * Adds or updates action.
     *
     * @param action action to be stored
     * @return -pos - 1 if action did not exist before;
     * pos if action exists;
     * pos is zero-based position in collection
     */
    int addAction(@NonNull LocationAction action);

    /**
     * Find related but not fulfilled action of given provider.
     *
     * @param provider related provider
     * @return related action or null if there is no related action
     */
    @Nullable
    LocationAction findRelatedNotFulfilledAction(@NonNull LocationActionSide provider);

    /**
     * Find action by its identifier.
     *
     * @param actionId action identifier
     * @return related action or null if not exists
     */
    @Nullable
    LocationAction find(long actionId);

    /**
     * Number of location actions.
     *
     * @return number of actions
     */
    int size();

    /**
     * Find location action by position.
     *
     * @param idx zero-based position index
     * @return related action or null if not exists
     */
    @Nullable
    LocationAction get(int idx);

    /**
     * Remove location action by index.
     *
     * @param idx index of action
     * @return index of action
     */
    int removeAction(int idx);
}
