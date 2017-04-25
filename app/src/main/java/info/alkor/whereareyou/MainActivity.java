package info.alkor.whereareyou;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Map;

import info.alkor.whereareyou.common.PermissionRequester;
import info.alkor.whereareyou.sms.SmsSender;

public class MainActivity extends AppCompatActivity {

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

	public void husbandWhereAreYouClicked(View view) {
		sender.send("+48605066798", this.getString(R.string.one_time_location_request));
	}

	public void wifeWhereAreYou(View view) {
		sender.send("+48601570960", this.getString(R.string.one_time_location_request));
	}
}
