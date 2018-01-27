package info.alkor.whereareyou.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.model.LocationActionSide;

/**
 * Location action sides view model.
 * Created by Marlena on 2018-01-23.
 */
public class LocationSideViewModel extends AndroidViewModel {

    private final LiveData<List<LocationActionSide>> model;

    public LocationSideViewModel(@NonNull Application application) {
        super(application);
        model = ((WhereAreYouContext) application).getUserDataAccess().getAllUsers();
    }

    public LiveData<List<LocationActionSide>> getModel() {
        return model;
    }
}
