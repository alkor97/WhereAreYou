package info.alkor.whereareyou.ui;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import info.alkor.whereareyou.R;

/**
 * View holder of single location request.
 * Created by Marlena on 2017-06-29.
 */
public class LocationActionViewHolder extends RecyclerView.ViewHolder {

    protected LocationAction locationAction;

    public LocationActionViewHolder(View itemView) {
        super(itemView);
    }

    public LocationAction getLocationAction() {
        return locationAction;
    }

    public void setLocationAction(LocationAction locationAction) {
        this.locationAction = locationAction;
        final Location location = locationAction.getLocation();

        TextView phone = (TextView) itemView.findViewById(R.id.row_main_phone_value);
        phone.setText(locationAction.getDisplayName() + "/" + locationAction.getPhoneNumber());

        TextView coords = (TextView) itemView.findViewById(R.id.row_main_coords_value);
        if (location != null) {
            coords.setText(locationAction.getLocation().getLatitude() + "," + locationAction.getLocation().getLongitude());
        } else {
            coords.setText("-");
        }
    }
}
