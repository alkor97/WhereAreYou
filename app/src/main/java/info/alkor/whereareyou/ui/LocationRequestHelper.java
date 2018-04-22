package info.alkor.whereareyou.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import java.util.Set;

import info.alkor.whereareyou.R;
import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.common.PermissionRequester;
import info.alkor.whereareyou.common.TextHelper;
import info.alkor.whereareyou.model.LocationActionSide;

public class LocationRequestHelper {

    private static final TextHelper TEXT_HELPER = new TextHelper();
    private final Context context;
    private final PermissionRequester permissionRequester;

    public LocationRequestHelper(@NonNull Context context, @NonNull PermissionRequester
            permissionRequester) {
        this.context = context;
        this.permissionRequester = permissionRequester;
    }

    private String getLocationConfirmationText(String displayName, String normalizedPhoneNumber) {
        final String formattedPhone = TEXT_HELPER.formatPhone(normalizedPhoneNumber);
        if (displayName == null) {
            return context.getString(R.string.confirm_locate_phone_no_name,
                    formattedPhone);
        } else {
            return context.getString(R.string.confirm_locate_phone,
                    displayName,
                    formattedPhone);
        }
    }

    public void confirmLocationRequest(final String displayName, final String
            phoneNumber) {
        final String normalizedPhoneNumber = TEXT_HELPER.formatPhone(phoneNumber);
        final String message = getLocationConfirmationText(displayName, normalizedPhoneNumber);
        PermissionRequester.ResultCallback callback = new PermissionRequester.ResultCallback() {
            @Override
            public void onPermissionRequestResult(Set<String> permissionRequestResult) {
                if (permissionRequestResult.contains(Manifest.permission.SEND_SMS)) {
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage(message)
                            .setPositiveButton(android.R.string.yes, new DialogInterface
                                    .OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestLocationOf(normalizedPhoneNumber, displayName);
                                }
                            }).setNegativeButton(android.R.string.no, null).show();
                }
            }
        };
        permissionRequester.requestAllPermissions(callback);
    }

    private void requestLocationOf(String normalizedPhoneNumber, String displayName) {
        getWhereAreYouContext()
                .getLocationQueryFlowManager()
                .sendLocationRequest(normalizedPhoneNumber, displayName);
        getWhereAreYouContext()
                .getUserDataAccess()
                .addUser(LocationActionSide.provider(normalizedPhoneNumber, displayName));
    }

    private WhereAreYouContext getWhereAreYouContext() {
        return (WhereAreYouContext) context.getApplicationContext();
    }
}
