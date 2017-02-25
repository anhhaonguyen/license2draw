package control.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.VideoView;

import org.rtspplayer.sample.R;


public class MainActivity  extends Activity implements OnClickListener {

	private static final String TAG  = "MainACtivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView but1 = (TextView)findViewById(R.id.but_losageles);
		TextView but2 = (TextView)findViewById(R.id.but_tokyo);
		TextView but3 = (TextView)findViewById(R.id.but_saigon);
		TextView but4 = (TextView)findViewById(R.id.but_yokohama);
		
		but1.setOnClickListener(this);
		but2.setOnClickListener(this);
		but3.setOnClickListener(this);
		but4.setOnClickListener(this);

		Log.e(TAG, "onCreate "+0);
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
			
		case R.id.but_saigon:
			Intent intent2 = new Intent(getApplicationContext(), TokyoActivity.class);
			intent2.putExtra("robot", "9000");
			startActivity(intent2);
			break;
			
		case R.id.but_tokyo:
			Intent intent3 = new Intent(getApplicationContext(), TokyoActivity.class);
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
