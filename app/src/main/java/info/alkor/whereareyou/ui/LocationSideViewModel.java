package info.alkor.whereareyou.ui;

import android.view.View;

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

    public String getNameOrPhone() {
        if (hasName()) {
            return side.getName();
        } else {
            return getFormattedPhone();
        }
    }

    public int getPhoneVisibility() {
        return hasName() ? View.VISIBLE : View.GONE;
    }

    private String getFormattedPhone() {
        return TEXT_HELPER.formatPhone(side.getPhone());
    }

    private String getDecoratedPhone() {
        return String.format("✆  %s", getFormattedPhone());
    }

    public String getPhone() {
        return getDecoratedPhone();
    }

    private boolean hasName() {
        return side.getName() != null;
    }
}
