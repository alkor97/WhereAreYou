package info.alkor.whereareyou.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.alkor.whereareyou.R;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationActions;

/**
 * Adapter for RecyclerView of location tasks.
 * Created by Marlena on 2017-06-29.
 */
public class LocationActionAdapter extends RecyclerView.Adapter<LocationActionViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LocationActions locationActions;

    public LocationActionAdapter(@NonNull Context context, @NonNull LocationActions locationActions) {
        this.context = context;
        this.locationActions = locationActions;
    }

    @Override
    public LocationActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main,
                parent, false);

        return new LocationActionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationActionViewHolder holder, int position) {
        final LocationAction entry = locationActions.get(position);
        holder.setLocationAction(entry);
    }

    @Override
    public int getItemCount() {
        return locationActions.size();
    }
}
