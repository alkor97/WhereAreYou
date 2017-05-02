package info.alkor.whereareyou;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import info.alkor.whereareyou.common.PermissionRequester;
import info.alkor.whereareyou.settings.CustomLogger;
import info.alkor.whereareyou.sms.SmsSender;

public class MainActivity extends AppCompatActivity {

	private static final int PICK_CONTACT = 1;
	private final PermissionRequester permissionRequester = new PermissionRequester(this);
	private final SmsSender sender = new SmsSender();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PermissionRequester.ResultCallback callback = new PermissionRequester.ResultCallback() {
			@Override
			public void onPermissionRequestResult(Map<String, Boolean> permissionRequestResult) {
			}
		};
		permissionRequester.requestAllPermissions(callback);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
	                                       @NonNull int[] grantResults) {
		permissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	public void locatePhone(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone
				.CONTENT_URI);
		startActivityForResult(intent, PICK_CONTACT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (PICK_CONTACT == requestCode && Activity.RESULT_OK == resultCode) {
			final Uri uri = data.getData();
			Cursor cursor = null;
			try {
				cursor = getContentResolver().query(uri, null, null, null, null);
				if (cursor != null) {
					final int displayNameIdx = cursor.getColumnIndex(ContactsContract
							.CommonDataKinds.Phone.DISPLAY_NAME);
					final int phoneIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds
							.Phone.DATA);

					if (cursor.moveToFirst()) {
						String displayName = cursor.getString(displayNameIdx);
						String phoneNumber = cursor.getString(phoneIdx).replaceAll("\\s+", "");
						confirmLocationRequest(displayName, phoneNumber);
					}
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
	}

	private void confirmLocationRequest(final String displayName, final String phoneNumber) {
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setMessage
				(getString(R.string.confirm_locate_phone, displayName, phoneNumber))
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				requestLocationOf(displayName, phoneNumber);
			}
		}).setNegativeButton(android.R.string.no, null).show();
	}

	private void requestLocationOf(String displayName, String phoneNumber) {
		sender.send(phoneNumber, this.getString(R.string.one_time_location_request));
		CustomLogger.clear();
		CustomLogger.requesting("location of " + phoneNumber + " (" + displayName + ")");
		// TODO: add request indication to list
	}

	public void refreshLogs(View view) {
		final TextView myLog = (TextView) findViewById(R.id.MyLog);
		myLog.setText(CustomLogger.get());
	}
}
