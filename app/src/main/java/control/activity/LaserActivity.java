package control.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;
import com.uudam.license2draw.R;

import java.net.URI;

import control.socket.WebSocketClient;
import control.util.AppConstant;
import control.util.AppStatic;
import veg.mediaplayer.sdk.MediaPlayer;


public class LaserActivity  extends BasePlayerActivity implements OnClickListener {

	private static final String TAG  = "LaserActivity";
	private WebSocketClient client, clientLocation;
	private boolean isActive = false;
	private boolean isLocationActive = false;
	
	private String robot;
	private ProgressDialog mLoadingDialog;
	private Activity activity;
	private boolean isShowAlert = false;
	
	private ImageView laser1, laser2, laser3, lazer4, lazer5, lazer6;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_laser);
		
		Log.e(TAG, "onCreate "+AppStatic.getSendingData("sample").toString());

		player = (MediaPlayer) findViewById(R.id.playerView);
		preparePlayer();

		mLoadingDialog = new ProgressDialog(LaserActivity.this);
		mLoadingDialog.setMessage("Loading...");
		mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mLoadingDialog.setCancelable(false);
		mLoadingDialog.show();
		
		activity = this;
		robot = getIntent().getExtras().getString("robot");
		
		laser1 = (ImageView)findViewById(R.id.laser1);
		laser2 = (ImageView)findViewById(R.id.laser2);
		laser3 = (ImageView)findViewById(R.id.laser3);
		lazer4 = (ImageView)findViewById(R.id.laser4);
		lazer5 = (ImageView)findViewById(R.id.laser5);
		lazer6 = (ImageView)findViewById(R.id.laser6);
		
		laser1.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						laser1.setBackgroundResource(R.drawable.laser_beam_pressed);
						v.setPressed(true);
						if (isActive) {
							client.send("lz1");
						}

						if(isLocationActive){
							clientLocation.send(AppStatic.getSendingData("lz1").toString());
						}
						break;
					case MotionEvent.ACTION_UP:
						laser1.setBackgroundResource(R.drawable.laser_beam);
						v.setPressed(false);
						if (isActive) {
							client.send("0");
						}

						break;
				}
				return true;
			}
		});
		
		laser2.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						laser2.setBackgroundResource(R.drawable.laser_beam_pressed);
						v.setPressed(true);
						if (isActive) {
							client.send("lz2");
						}

						if(isLocationActive){
							clientLocation.send(AppStatic.getSendingData("lz2").toString());
						}
						break;
					case MotionEvent.ACTION_UP:
						laser2.setBackgroundResource(R.drawable.laser_beam);
						v.setPressed(false);
						if (isActive) {
							client.send("0");
						}

						break;
				}
				return true;
			}
		});
		
		laser3.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						laser3.setBackgroundResource(R.drawable.laser_beam_pressed);
						v.setPressed(true);
						if (isActive) {
							client.send("lz3");
						}

						if(isLocationActive){
							clientLocation.send(AppStatic.getSendingData("lz3").toString());
						}
						break;
					case MotionEvent.ACTION_UP:
						laser3.setBackgroundResource(R.drawable.laser_beam);
						v.setPressed(false);
						if (isActive) {
							client.send("0");
						}

						break;
				}
				return true;
			}
		});

		lazer4.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						lazer4.setBackgroundResource(R.drawable.laser_beam_pressed);
						v.setPressed(true);
						if (isActive) {
							client.send("lz4");
						}

						if(isLocationActive){
							clientLocation.send(AppStatic.getSendingData("lz4").toString());
						}
						break;
					case MotionEvent.ACTION_UP:
						lazer4.setBackgroundResource(R.drawable.laser_beam);
						v.setPressed(false);
						if (isActive) {
							client.send("0");
						}

						break;
				}
				return true;
			}
		});

		lazer5.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						lazer5.setBackgroundResource(R.drawable.laser_beam_pressed);
						v.setPressed(true);
						if (isActive) {
							client.send("lz5");
						}

						if(isLocationActive){
							clientLocation.send(AppStatic.getSendingData("lz5").toString());
						}
						break;
					case MotionEvent.ACTION_UP:
						lazer5.setBackgroundResource(R.drawable.laser_beam);
						v.setPressed(false);
						if (isActive) {
							client.send("0");
						}

						break;
				}
				return true;
			}
		});

		lazer6.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						lazer6.setBackgroundResource(R.drawable.laser_beam_pressed);
						v.setPressed(true);
						if (isActive) {
							client.send("lz6");
						}

						if(isLocationActive){
							clientLocation.send(AppStatic.getSendingData("lz6").toString());
						}
						break;
					case MotionEvent.ACTION_UP:
						lazer6.setBackgroundResource(R.drawable.laser_beam);
						v.setPressed(false);
						if (isActive) {
							client.send("0");
						}

						break;
				}
				return true;
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();

		onStartStream();
		
		SocketClient.getInstances().setInfo("abc", "123123", robot);
		SocketClient.getInstances().connect(new Notification() {
			
			@Override
			public void notifyMessage(String message) {
				// TODO Auto-generated method stub
				Log.v(TAG, "Message "+message);
				//Toast.makeText(LoginActivity.this, message, 1000).show();
				if(activity != null){
					if(mLoadingDialog != null && mLoadingDialog.isShowing()){
						mLoadingDialog.dismiss();
					}

					if(message.contains("norobot")){
						isShowAlert = true;
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								showAlert("No Robot", "There is no robot with this name");
							}
						});
					}
				}
			}
			
			@Override
			public void notifyError() {
				// TODO Auto-generated method stub
				if(activity != null){
					if(mLoadingDialog != null && mLoadingDialog.isShowing()){
						mLoadingDialog.dismiss();
					}
				}
			}
			
			@Override
			public void notifyDisconnected() {
				// TODO Auto-generated method stub
				Log.e(TAG, "notifyDisconnected");
				
				if(activity != null){
					if(mLoadingDialog != null && mLoadingDialog.isShowing()){
						mLoadingDialog.dismiss();
					}
					
					if(!isShowAlert){
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								isShowAlert = true;
								showAlert("Timeout", "Your session is ended. System will disconnect now. Please help us spread this remote to the world");
							}
						});
					}
				}
			}
			
			@Override
			public void notifyConnected() {
				// TODO Auto-generated method stub
				Log.i(TAG, "Connected");
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					//Toast.makeText(LoginActivity.this, "Successful", 1000).show();
					mLoadingDialog.dismiss();
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showSuccess();
						}
					});
				}
			}
		});
		
		if (SocketClient.getInstances().getClient() == null) {
			isActive = false;
		} else {
			isActive = true;
			client = SocketClient.getInstances().getClient();
		}


		String port3388Url = AppConstant.ROBOT_URL + "3388";
		clientLocation = new WebSocketClient(URI.create(port3388Url), new WebSocketClient.Listener() {

			@Override
			public void onMessage(byte[] data) {
				// TODO Auto-generated method stub
				Log.d(TAG, String.format("Got location binary message! %s", data));
			}

			@Override
			public void onMessage(String message) {
				// TODO Auto-generated method stub
				Log.d(TAG, String.format("Got location string message! %s", message));
			}

			@Override
			public void onError(Exception error) {
				// TODO Auto-generated method stub
				Log.e(TAG, "Error!", error);
				final String errorString = error.getMessage();
				if(!isShowAlert){
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							isShowAlert = true;
							showAlert("Error", errorString);
						}
					});
				}
			}

			@Override
			public void onDisconnect(int code, String reason) {
				// TODO Auto-generated method stub
				Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
			}

			@Override
			public void onConnect() {
				// TODO Auto-generated method stub
				Log.d(TAG, "Connected! Location");
				clientLocation.send("Laser Location hello");
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							showSuccess();
						}
					});
				}
			}
		}, null);

		clientLocation.connect();


	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(getApplicationContext(), ControlActivity.class);
		switch (v.getId()) {
		case R.id.but_losageles:
			intent.putExtra("robot", "4000");
			startActivity(intent);
			break;
			
		case R.id.but_saigon_evo2:
			intent.putExtra("robot", "3000");
			startActivity(intent);
			break;
			
		case R.id.but_saigon_evo_1:
			intent.putExtra("robot", "9000");
			startActivity(intent);
			break;
			
		case R.id.but_yokohama:
			intent.putExtra("robot", "3000");
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	private void showAlert(String title, String message) {
		Log.e(TAG, "showAlert");
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LaserActivity.this);
		alertDialogBuilder.setCancelable(true);
		alertDialogBuilder.setTitle(title);
		if(!message.equals("")){
			alertDialogBuilder.setMessage(message);
		}
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				findViewById(R.id.but_center).setBackgroundResource(R.drawable.icon_lost);
			}
		});
		
		if(title.equals("Timeout")){
			alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				}
			});
		}

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	private void showSuccess(){
		Log.e(TAG, "showSuccess");
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set title
		alertDialogBuilder.setTitle("Done");
		alertDialogBuilder.setMessage("Welcome to License to Draw. Your 5 minutes Drawing session begins now.")
		.setCancelable(false)
		.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
//						findViewById(R.id.but_center).setBackgroundResource(R.drawable.icon_connected);
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
//		alertDialog.show();
	}
	
	private void showAlertTimout(String title, String message) {
		Log.e(TAG, "showAlertTimout");
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LaserActivity.this);
		alertDialogBuilder.setCancelable(true);
		alertDialogBuilder.setTitle(title);
		if(!message.equals("")){
			alertDialogBuilder.setMessage(message);
		}
		alertDialogBuilder.setCancelable(false).setPositiveButton("Not now", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}


}
