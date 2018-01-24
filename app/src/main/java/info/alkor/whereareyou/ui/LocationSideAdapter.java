package info.alkor.whereareyou.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import info.alkor.whereareyou.R;
import info.alkor.whereareyou.model.LocationActionSide;

/**
 * Location side recycler view adapter.
 * Created by Marlena on 2018-01-23.
 */
public class LocationSideAdapter extends RecyclerView.Adapter<LocationSideViewHolder> {

    private List<LocationActionSide> sides = Collections.emptyList();

    @Override
    public LocationSideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_side,
                parent,
                false);
        return new LocationSideViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(LocationSideViewHolder holder, int position) {
        holder.bind(sides.get(position));
    }

    @Override
    public int getItemCount() {
        return sides.size();
    }

    public List<LocationActionSide> getSides() {
        return sides;
    }

    public void setSides(List<LocationActionSide> sides) {
        this.sides = sides;
    }
}
