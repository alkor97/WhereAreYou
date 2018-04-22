package info.alkor.whereareyou.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.HashSet;
import java.util.Set;

/**
 * Permission accessor.
 * Created by Marlena on 2017-04-02.
 */
class PermissionAccessor {

    private final Context context;

    PermissionAccessor(@NonNull Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    String[] retrievePermissions() {
        try {
            return getContext().getPackageManager().getPackageInfo(getContext().getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean isPermissionGranted(@NonNull String permission) {
        return ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager
                .PERMISSION_GRANTED;
    }

    private boolean isPermissionDenied(@NonNull String permission) {
        return ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager
                .PERMISSION_DENIED;
    }

    Set<String> filterDeniedPermissions(String... permissions) {
        final Set<String> deniedPermissions = new HashSet<>();
        for (String permission : permissions) {
            if (isPermissionDenied(permission)) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    Set<String> getPermissions() {
        Set<String> result = new HashSet<>();
        for (String permission : retrievePermissions()) {
            if (isPermissionGranted(permission)) {
                result.add(permission);
            }
        }
        return result;
    }
}
