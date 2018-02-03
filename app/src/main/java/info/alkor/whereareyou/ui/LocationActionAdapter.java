package info.alkor.whereareyou.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import info.alkor.whereareyou.R;
import info.alkor.whereareyou.model.LocationAction;

/**
 * Location action adapter.
 * Created by Marlena on 2018-01-28.
 */
public class LocationActionAdapter extends RecyclerView.Adapter<LocationActionViewHolder> {

    private List<LocationAction> actions = Collections.emptyList();

    @Override
    public LocationActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_action,
                parent,
                false);
        return new LocationActionViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(LocationActionViewHolder holder, int position) {
        holder.bind(actions.get(position));
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    public List<LocationAction> getActions() {
        return actions;
    }

    public void setActions(List<LocationAction> actions) {
        this.actions = actions;
    }
}
