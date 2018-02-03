package info.alkor.whereareyou;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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

import info.alkor.whereareyou.model.LocationActionSide;
import info.alkor.whereareyou.ui.LocationSidesAdapter;
import info.alkor.whereareyou.ui.LocationSidesViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private LocationSidesAdapter adapter;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        RecyclerView rv = view.findViewById(R.id.locationSides);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());

        adapter = new LocationSidesAdapter();
        rv.setAdapter(adapter);

        LocationSidesViewModel viewModel = getViewModel();
        if (viewModel != null) {
            viewModel.getModel().observe(this, new SidesObserver());
        }

        return view;
    }

    private LocationSidesViewModel getViewModel() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            return ViewModelProviders.of(getActivity()).get(LocationSidesViewModel.class);
        }
        return null;
    }

    private class SidesObserver implements Observer<List<LocationActionSide>> {
        @Override
        public void onChanged(@Nullable List<LocationActionSide> locationActionSides) {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                    new LocationSideDiffCallback(
                            adapter.getSides(),
                            locationActionSides != null
                                    ? locationActionSides
                                    : Collections.<LocationActionSide>emptyList()));

            adapter.getSides().clear();
            adapter.setSides(locationActionSides);
            result.dispatchUpdatesTo(adapter);
        }
    }

    private class LocationSideDiffCallback extends DiffUtil.Callback {

        private final List<LocationActionSide> oldList;
        private final List<LocationActionSide> newList;

        LocationSideDiffCallback(@NonNull List<LocationActionSide> oldList, @NonNull List<LocationActionSide> newList) {
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
            return oldList.get(oldItemPosition).getPhone().equals(newList.get(newItemPosition).getPhone());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return areItemsTheSame(oldItemPosition, newItemPosition);
        }
    }
}
