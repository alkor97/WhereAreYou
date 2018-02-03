package info.alkor.whereareyou.android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;

/**
 * Contacts helper.
 * Created by Marlena on 2018-01-01.
 */
public class ContactsHelper {

    private final Context context;

    public ContactsHelper(Context context) {
        this.context = context;
    }

    public String getDisplayName(String phone) {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)) {
            final Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));

            final Cursor contactLookup = context.getContentResolver().query(uri, new String[]{BaseColumns._ID,
                    ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

            try {
                if (contactLookup != null && contactLookup.getCount() > 0) {
                    contactLookup.moveToNext();
                    return contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                }
            } finally {
                if (contactLookup != null) {
                    contactLookup.close();
                }
            }
        }

        return null;
    }
}
