package info.alkor.whereareyou;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.Map;

import info.alkor.whereareyou.common.PermissionRequester;

public class MainActivity extends AppCompatActivity {

	private final PermissionRequester permissionRequester = new PermissionRequester(this);

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
}
