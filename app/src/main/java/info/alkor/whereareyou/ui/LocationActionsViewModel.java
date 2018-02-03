package info.alkor.whereareyou.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.model.LocationAction;

/**
 * Location actions view model.
 * Created by Marlena on 2018-01-28.
 */
public class LocationActionsViewModel extends AndroidViewModel {

    private final LiveData<List<LocationAction>> model;

    public LocationActionsViewModel(@NonNull Application application) {
        super(application);
        this.model = ((WhereAreYouContext) application).getActionDataAccess().getAllActionsLive();
    }

    public LiveData<List<LocationAction>> getModel() {
        return model;
    }
}
