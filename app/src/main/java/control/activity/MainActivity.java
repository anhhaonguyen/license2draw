package control.activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import org.rtspplayer.sample.R;

import control.util.AppStatic;
import control.util.LocationController;


public class MainActivity  extends FragmentActivity implements OnClickListener, LocationController.LocationControllerListener{

	private static final String TAG  = "MainACtivity";

	private LocationController locationController;

	@Override
	public void onFail() {

	}

	@Override
	public void onLocationChanged(Location location) {
		AppStatic.location = location;

		Log.e(TAG, "onLocationChange "+location.getLongitude());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().hide();
		
		TextView but1 = (TextView)findViewById(R.id.but_losageles);
		TextView but2 = (TextView)findViewById(R.id.but_saigon_evo2);
		TextView but3 = (TextView)findViewById(R.id.but_saigon_evo_1);
		TextView but4 = (TextView)findViewById(R.id.but_yokohama);
		
		but1.setOnClickListener(this);
		but2.setOnClickListener(this);
		but3.setOnClickListener(this);
		but4.setOnClickListener(this);

		locationController = new LocationController(this);
		locationController.setLocationListener(this);

		AppStatic.location = new Location("");

		Log.e(TAG, "onCreate "+0);
	}

	@Override
	protected void onStart() {
		super.onStart();
		locationController.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		locationController.disconnect();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.but_losageles:
			Intent intent = new Intent(getApplicationContext(), ControlActivity.class);
			intent.putExtra("robot", "4000");
			intent.putExtra("laser", "9000");
			startActivity(intent);
			break;
			
		case R.id.but_saigon_evo_1:
			Intent intent2 = new Intent(getApplicationContext(), EvolutionActivity.class);
			intent2.putExtra("robot", "9000");
			startActivity(intent2);
			break;
			
		case R.id.but_saigon_evo2:
			Intent intent3 = new Intent(getApplicationContext(), EvolutionActivity.class);
			intent3.putExtra("robot", "8000");
			startActivity(intent3);
			break;
			
		case R.id.but_yokohama:
			Intent intent4 = new Intent(getApplicationContext(), LaserActivity.class);
			intent4.putExtra("robot", "9000");
			startActivity(intent4);
			break;

		default:
			break;
		}
	}
}
