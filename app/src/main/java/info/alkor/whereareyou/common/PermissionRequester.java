package info.alkor.whereareyou.common;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Permission requester.
 * Created by Marlena on 2017-04-02.
 */
public class PermissionRequester extends PermissionAccessor {

    private final Set<ResultCallback> requests = new HashSet<>();

    public PermissionRequester(@NonNull AppCompatActivity activity) {
        super(activity);
    }

    protected AppCompatActivity getContext() {
        return (AppCompatActivity) super.getContext();
    }

    private void requestPermissions(@NonNull ResultCallback resultCallback, String...
            permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final Set<String> deniedPermissions = filterDeniedPermissions(permissions);
            if (!deniedPermissions.isEmpty()) {
                requests.add(resultCallback);
                getContext().requestPermissions(deniedPermissions.toArray(new
                        String[deniedPermissions.size()]), resultCallback.hashCode());
            } else {
                // execute callback immediately
                resultCallback.onPermissionRequestResult(toMap(permissions));
            }
        } else {
            // execute callback immediately
            resultCallback.onPermissionRequestResult(toMap(permissions));
        }
    }

    public void requestAllPermissions(@NonNull ResultCallback resultCallback) {
        requestPermissions(resultCallback, retrievePermissions());
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Iterator<ResultCallback> it = requests.iterator();
        while (it.hasNext()) {
            ResultCallback request = it.next();
            Map<String, Boolean> map = new HashMap<>();
            if (requestCode == request.hashCode()) {
                int idx = -1;
                for (String permission : permissions) {
                    ++idx;
                    map.put(permission, grantResults[idx] == PackageManager.PERMISSION_GRANTED);
                }
                it.remove();
                request.onPermissionRequestResult(map);
                break;
            }
        }
    }

    public interface ResultCallback {
        void onPermissionRequestResult(Map<String, Boolean> permissionRequestResult);
    }
}
