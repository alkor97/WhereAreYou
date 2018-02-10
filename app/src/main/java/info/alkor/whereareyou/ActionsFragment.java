package info.alkor.whereareyou;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.model.LocationActionSide;
import info.alkor.whereareyou.ui.LocationActionAdapter;
import info.alkor.whereareyou.ui.LocationActionsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActionsFragment extends Fragment {

    private LocationActionAdapter adapter;

    public ActionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_actions, container, false);

        RecyclerView rv = view.findViewById(R.id.locationActions);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());

        adapter = new LocationActionAdapter();
        rv.setAdapter(adapter);

        LocationActionsViewModel viewModel = getViewModel();
        if (viewModel != null) {
            viewModel.getModel().observe(this, new ActionsObserver());
        }

        return view;
    }

    private LocationActionsViewModel getViewModel() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            return ViewModelProviders.of(getActivity()).get(LocationActionsViewModel.class);
        }
        return null;
    }

    private class ActionsObserver implements Observer<List<LocationAction>> {
        @Override
        public void onChanged(@Nullable List<LocationAction> locationActions) {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                    new LocationActionDiffCallback(
                            adapter.getActions(),
                            locationActions != null
                                    ? locationActions
                                    : Collections.<LocationAction>emptyList()));

            adapter.getActions().clear();
            adapter.setActions(locationActions);
            result.dispatchUpdatesTo(adapter);
        }
    }

    private class LocationActionDiffCallback extends DiffUtil.Callback {

        private final List<LocationAction> oldList;
        private final List<LocationAction> newList;

        LocationActionDiffCallback(@NonNull List<LocationAction> oldList, @NonNull
                List<LocationAction> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getActionId() == newList.get(newItemPosition)
                    .getActionId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            final LocationAction a = oldList.get(oldItemPosition);
            final LocationAction b = newList.get(newItemPosition);

            return a.getActionId() == b.getActionId()
                    && areEqual(a.getDeliveryStatus(), b.getDeliveryStatus())
                    && areEqual(a.getState(), b.getState())
                    && areEqual(a.getDeliveryStatus(), b.getDeliveryStatus())
                    && areEqual(a.getSide(), b.getSide())
                    && areEqual(a.getLocation(), b.getLocation());
        }

        private boolean areEqual(Location a, Location b) {
            return a == b || a != null && b != null
                    && areEqual(a.getProvider(), b.getProvider())
                    && a.getTime() == b.getTime()
                    && a.getLatitude() == b.getLatitude()
                    && a.getLongitude() == b.getLongitude()
                    && a.getAltitude() == b.getAltitude()
                    && a.getAccuracy() == b.getAccuracy()
                    && a.getBearing() == b.getBearing()
                    && a.getSpeed() == b.getSpeed();
        }

        private boolean areEqual(LocationActionSide a, LocationActionSide b) {
            return a == b || a != null && b != null
                    && areEqual(a.getType(), b.getType())
                    && areEqual(a.getPhone(), b.getPhone())
                    && areEqual(a.getName(), b.getName());
        }

        private <E> boolean areEqual(E a, E b) {
            return a == b || a != null && a.equals(b);
        }
    }
}
