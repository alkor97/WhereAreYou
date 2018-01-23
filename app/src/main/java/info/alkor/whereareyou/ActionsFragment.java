package info.alkor.whereareyou;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.alkor.whereareyou.model.LocationActions;
import info.alkor.whereareyou.receivers.LocationActionsReceiver;
import info.alkor.whereareyou.senders.LocationActionsSender;
import info.alkor.whereareyou.ui.LocationActionAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActionsFragment extends Fragment {

    private LocationActionsReceiver actionsHandler;

    public ActionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_actions, container, false);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.locationActions);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());

        LocationActions model = getWhereAreYouContext().getModel();
        LocationActionsSender helper = getWhereAreYouContext().getActionsSender();

        LocationActionAdapter adapter = new LocationActionAdapter(model);
        rv.setAdapter(adapter);

        actionsHandler = new LocationActionsReceiver(helper, adapter);
        actionsHandler.registerHandler(getContext());

        return view;
    }

    @Override
    public void onDestroyView() {
        actionsHandler.unregisterHandler(getContext());
        super.onDestroyView();
    }

    private WhereAreYouContext getWhereAreYouContext() {
        return (WhereAreYouContext) getActivity().getApplicationContext();
    }
}
