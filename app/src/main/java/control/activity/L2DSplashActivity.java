package control.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.uudam.license2draw.R;


public class L2DSplashActivity extends Activity {
	private static final String TAG = "L2DSplashActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		
		load();
	}

	private void load(){
		Thread logoTimer = new Thread() {
			public void run() {
				try {
					sleep(500);
					startActivity(new Intent(L2DSplashActivity.this,
							MainActivity.class));
					finish();
				}

				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				finally {
					finish();
				}
			}
		};

		logoTimer.start();
	}
}
