package info.alkor.whereareyou;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import info.alkor.whereareyou.batery.BatteryLevelProvider;
import info.alkor.whereareyou.location.LocationProvider;
import info.alkor.whereareyou.settings.ApplicationSettings;
import info.alkor.whereareyou.settings.LocationSettings;

public class MainActivity extends AppCompatActivity {

	private ApplicationSettings settings = new ApplicationSettings(new LocationSettings());
	private BatteryLevelProvider batteryLevelProvider = new BatteryLevelProvider(this);
	private LocationProvider locationProvider = new LocationProvider(this, settings
			.getLocationSettings()) {
		@Override
		public void onLocationProvided(Location location) {
			MainActivity.this.onLocationProvided(location);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		locationProvider.requestLocationUpdates();
	}

	@Override
	protected void onDestroy() {
		locationProvider.cancelLocationUpdates();
		super.onDestroy();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
	                                       @NonNull int[] grantResults) {
		if (requestCode == locationProvider.hashCode() && grantResults.length > 0 &&
				grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			locationProvider.requestLocationUpdates();
		}
	}

	private void onLocationProvided(Location location) {
		TextView tv = (TextView) this.findViewById(R.id.textOut);
		tv.setText(location.getLatitude() + "," + location.getLongitude() + "\n" + (int) 100 *
				batteryLevelProvider.getCurrentBatteryLevel() + "%");
	}
}
