package info.alkor.whereareyou.common;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Permission requester.
 * Created by Marlena on 2017-04-02.
 */
public class PermissionRequester extends PermissionAccessor {

    private final Set<ResultCallback> requests = new HashSet<>();

    public PermissionRequester(@NonNull Context context) {
        super(context);
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
                resultCallback.onPermissionRequestResult(getPermissions());
            }
        } else {
            // execute callback immediately
            resultCallback.onPermissionRequestResult(getPermissions());
        }
    }

    public void requestAllPermissions(@NonNull ResultCallback resultCallback) {
        requestPermissions(resultCallback, retrievePermissions());
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        Iterator<ResultCallback> it = requests.iterator();
        while (it.hasNext()) {
            ResultCallback request = it.next();
            if (requestCode == request.hashCode()) {
                it.remove();
                request.onPermissionRequestResult(getPermissions());
                break;
            }
        }
    }

    public interface ResultCallback {
        void onPermissionRequestResult(@NonNull Set<String> permissionRequestResult);
    }
}
