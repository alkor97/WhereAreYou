package info.alkor.whereareyou.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import info.alkor.whereareyou.model.LocationActionSide;

/**
 * Location action sides view model.
 * Created by Marlena on 2018-01-23.
 */
public class LocationSideViewModel extends ViewModel {

    private final MutableLiveData<List<LocationActionSide>> model = new MutableLiveData<>();

    public MutableLiveData<List<LocationActionSide>> getModel() {
        return model;
    }
}
