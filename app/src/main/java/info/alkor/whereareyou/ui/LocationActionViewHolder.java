package info.alkor.whereareyou.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import info.alkor.whereareyou.R;
import info.alkor.whereareyou.common.TextHelper;
import info.alkor.whereareyou.location.minimal.MinimalLocationFormatter;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationActionSide;

/**
 * View holder of single location request.
 * Created by Marlena on 2017-06-29.
 */
class LocationActionViewHolder extends RecyclerView.ViewHolder {

    private static final MinimalLocationFormatter FORMATTER = new MinimalLocationFormatter();
    private static final String[] DIRECTIONS = {"↑N", "↗NE", "→E", "↘SE", "↓S", "↙SW", "←W", "↖NW"};
    private static final TextHelper TEXT_HELPER = new TextHelper();

    LocationActionViewHolder(View itemView) {
        super(itemView);
    }

    void setLocationAction(LocationAction action) {
        showTime(action);
        showDeliveryStatus(action);
        showSide(action);
        final Location location = action.getLocation();
        if (location != null) {
            showCoordinates(location);
            showAltitude(location);
            showAccuracy(location);
            showAzimuth(location);
            showSpeed(location);
            super.itemView.setOnClickListener(new ClickHandler(action));
        }
    }

    private void showTime(LocationAction action) {
        final long time = action.getLocation() != null
                ? action.getLocation().getTime()
                : System.currentTimeMillis();

        String value = DateUtils.formatDateTime(getContext(), time, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_WEEKDAY);
        TextView textView = (TextView) itemView.findViewById(R.id.row_main_time_value);
        textView.setText(value);
    }

    private String getDeliverStatus(LocationAction.DeliveryStatus status) {
        if (status == LocationAction.DeliveryStatus.SENT) {
            return "➜";
        } else if (status == LocationAction.DeliveryStatus.DELIVERED) {
            return "✓";
        }
        return "…";
    }

    private void showDeliveryStatus(LocationAction action) {
        TextView textView = (TextView) itemView.findViewById(R.id.row_main_delivery_value);
        textView.setText(getDeliverStatus(action.getDeliveryStatus()));
    }

    private String getSideSymbol(LocationActionSide.Type type) {
        if (type == LocationActionSide.Type.REQUESTER) {
            return "➘";
        }
        return "➚";
    }

    private void showSide(LocationAction action) {
        TextView textView = (TextView) itemView.findViewById(R.id.row_main_phone_value);
        String value;
        if (action.getDisplayName() != null) {
            value = getContext().getString(R.string.row_main_side,
                    action.getDisplayName(),
                    TEXT_HELPER.formatPhone(action.getPhoneNumber()),
                    getSideSymbol(action.getSide().getType()));
        } else {
            value = getContext().getString(R.string.row_main_side_without_name,
                    TEXT_HELPER.formatPhone(action.getPhoneNumber()),
                    getSideSymbol(action.getSide().getType()));
        }
        textView.setText(value);
    }

    private void showCoordinates(Location location) {
        TextView textView = (TextView) itemView.findViewById(R.id.row_main_coordinates_value);
        textView.setVisibility(View.VISIBLE);
        String value = getContext().getString(R.string.row_main_position,
                location.getLatitude(),
                location.getLongitude(),
                location.getProvider());
        textView.setText(value);
    }

    private void showAltitude(Location location) {
        if (location.hasAltitude()) {
            TextView textView = (TextView) itemView.findViewById(R.id.row_main_altitude_value);
            textView.setVisibility(View.VISIBLE);
            String value = getContext().getString(R.string.row_main_altitude,
                    location.getAltitude(),
                    "m");
            textView.setText(value);
        }
    }

    private void showAccuracy(Location location) {
        if (location.hasAccuracy()) {
            TextView textView = (TextView) itemView.findViewById(R.id.row_main_accuracy_value);
            textView.setVisibility(View.VISIBLE);
            String value = getContext().getString(R.string.row_main_accuracy,
                    location.getAccuracy(),
                    "m");
            textView.setText(value);
        }
    }

    private String getDirection(float bearing) {
        float normalized = (bearing - 22.5f) / 45;
        return DIRECTIONS[(int) Math.ceil(normalized) % DIRECTIONS.length];
    }

    private void showAzimuth(Location location) {
        if (location.hasBearing()) {
            TextView textView = (TextView) itemView.findViewById(R.id.row_main_azimuth_value);
            textView.setVisibility(View.VISIBLE);
            String value = getContext().getString(R.string.row_main_azimuth,
                    location.getBearing(),
                    getDirection(location.getBearing()));
            textView.setText(value);
        }
    }

    private void showSpeed(Location location) {
        if (location.hasSpeed()) {
            TextView textView = (TextView) itemView.findViewById(R.id.row_main_speed_value);
            textView.setVisibility(View.VISIBLE);
            String value = getContext().getString(R.string.row_main_speed,
                    location.getSpeed(),
                    "m/s");
            textView.setText(value);
        }
    }

    private Context getContext() {
        return itemView.getContext();
    }

    private class ClickHandler implements View.OnClickListener {

        private Intent intent;

        ClickHandler(LocationAction action) {
            String template = getContext().getString(R.string.location_presenter_url,
                    FORMATTER.format(action.getLocation()),
                    TEXT_HELPER.encode(action.getPhoneNumber()),
                    TEXT_HELPER.encode(action.getDisplayName()));
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(template));
        }

        @Override
        public void onClick(View view) {
            getContext().startActivity(intent);
        }
    }
}
