package info.alkor.whereareyou.ui;

import info.alkor.whereareyou.common.TextHelper;
import info.alkor.whereareyou.model.LocationActionSide;

/**
 * Location side view model.
 * Created by Marlena on 2018-01-30.
 */
public class LocationSideViewModel {

    private static final TextHelper TEXT_HELPER = new TextHelper();
    private final LocationActionSide side;

    LocationSideViewModel(LocationActionSide side) {
        this.side = side;
    }

    public String getName() {
        if (side.getName() != null) {
            return side.getName() + " (" + TEXT_HELPER.formatPhone(side.getPhone()) + ")";
        } else {
            return TEXT_HELPER.formatPhone(side.getPhone());
        }
    }
}
