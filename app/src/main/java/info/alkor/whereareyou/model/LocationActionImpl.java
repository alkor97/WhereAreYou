package info.alkor.whereareyou.model;

import android.support.annotation.NonNull;

/**
 * Writable location action.
 * Created by Marlena on 2018-02-02.
 */
public class LocationActionImpl extends LocationAction {
    public LocationActionImpl(long actionId, @NonNull LocationActionSide side, @NonNull State state) {
        super(actionId, side, state);
    }
}
