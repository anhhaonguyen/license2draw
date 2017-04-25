package control.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;


import com.uudam.license2draw.R;

import java.net.URI;

import control.socket.WebSocketClient;
import control.util.AppConstant;
import veg.mediaplayer.sdk.MediaPlayer;


public class ControlActivity extends BasePlayerActivity implements
		OnClickListener, OnTouchListener, Notification {

	private static final String TAG = "ControlActivity";
	private ImageView imgLost;
	private boolean isActive = false;
	private boolean isLaserActive = false;
	
	private WebSocketClient client;
	private WebSocketClient laserClient;
	private String robot;
	private String laser;
	private ProgressDialog mLoadingDialog;
	private Activity activity;
	private boolean isShowAlert = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);

		player = (MediaPlayer) findViewById(R.id.playerView);
		preparePlayer();

		getActionBar().hide();
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		final int height = displayMetrics.heightPixels;
		final int width = displayMetrics.widthPixels;
		int SCREEN_H = height;
		int SCREEN_W = width;
		
		activity = this;
		Log.e(TAG, "W "+SCREEN_W+" "+SCREEN_H);
		
		mLoadingDialog = new ProgressDialog(ControlActivity.this);
		mLoadingDialog.setMessage("Loading...");
		mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mLoadingDialog.setCancelable(false);
		mLoadingDialog.show();

		robot = getIntent().getExtras().getString("robot");
		laser = getIntent().getExtras().getString("laser");
		
		String robotURL = AppConstant.ROBOT_URL + robot;
		String laserURL = AppConstant.LASER_URL + laser;
		
		client = new WebSocketClient(URI.create(robotURL), new WebSocketClient.Listener() {
			
			@Override
			public void onMessage(byte[] data) {
				// TODO Auto-generated method stub
				Log.d(TAG, String.format("Got binary message! %s", data));
			}
			
			@Override
			public void onMessage(String message) {
				// TODO Auto-generated method stub
				Log.d(TAG, String.format("Got string message! %s", message));
			}
			
			@Override
			public void onError(Exception error) {
				// TODO Auto-generated method stub
				Log.e(TAG, "Error!", error);
				isActive = false;
			}
			
			@Override
			public void onDisconnect(int code, String reason) {
				// TODO Auto-generated method stub
				Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
				isActive = false;
			}
			
			@Override
			public void onConnect() {
				// TODO Auto-generated method stub
				Log.d(TAG, "Connected!");
				client.send("Hello");
				isActive = true;
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
		
		laserClient = new WebSocketClient(URI.create(laserURL), new WebSocketClient.Listener() {
			
			@Override
			public void onMessage(byte[] data) {
				// TODO Auto-generated method stub
				Log.d(TAG, String.format("Got binary message! %s", data));
			}
			
			@Override
			public void onMessage(String message) {
				// TODO Auto-generated method stub
				Log.d(TAG, String.format("Got string message! %s", message));
			}
			
			@Override
			public void onError(Exception error) {
				// TODO Auto-generated method stub
				Log.e(TAG, "Error!", error);
				isLaserActive = false;
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
				isLaserActive = false;
			}
			
			@Override
			public void onConnect() {
				// TODO Auto-generated method stub
				Log.d(TAG, "Connected!");
				laserClient.send("Laser hello");
				isLaserActive = true;
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
		
		ImageView imgConnect = (ImageView) findViewById(R.id.laser1);
		ImageView imgSquare = (ImageView)findViewById(R.id.laser2);
		ImageView imgForward = (ImageView) findViewById(R.id.but_forward);
		ImageView imgBackward = (ImageView) findViewById(R.id.but_backward);
		imgLost = (ImageView) findViewById(R.id.img_state_connect);
//		imgLost.setVisibility(View.GONE);
		
		ImageView imgOval = (ImageView)findViewById(R.id.laser3);
		imgOval.setOnClickListener(this);
		imgConnect.setOnClickListener(this);
		imgSquare.setOnClickListener(this);
		

		ImageView imgLeft = (ImageView) findViewById(R.id.but_left);
		ImageView imgRight = (ImageView) findViewById(R.id.but_right);
		
		final ImageView imgLaser = (ImageView) findViewById(R.id.laser_beam);

		imgConnect.setOnClickListener(this);
		imgLaser.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch(action) {
					case MotionEvent.ACTION_DOWN:
						v.setPressed(true);
//						((ImageView) v).setBackgroundResource(R.id.but_down_left);
						if (isActive) {
							laserClient.send("lz");
						}
						break;
					case MotionEvent.ACTION_UP:
						v.setPressed(false);
						if (isActive) {
							laserClient.send("0");
						}
						break;
					default:
						break;
				}
				return true;
			}
		});
		
		imgBackward.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					v.setPressed(true);
					if (isActive) {
						client.send("8");
					}
					break;
					
				case MotionEvent.ACTION_UP:
					v.setPressed(false);
					if (isActive) {
						client.send("5");
					}
					break;

				default:
					break;
				}
				
				return true;
			}
		});
		
		imgForward.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					v.setPressed(true);
					if (isActive) {
						client.send("2");
					}
					break;
					
				case MotionEvent.ACTION_UP:
					v.setPressed(false);
					if (isActive) {
						client.send("5");
					}
					break;

				default:
					break;
				}
				return true;
			}
		});
		
		imgLeft.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					v.setPressed(true);
					if (isActive) {
						client.send("1");
					}
					break;
					
				case MotionEvent.ACTION_UP:
					v.setPressed(false);
					if (isActive) {
						client.send("0");
					}
					break;

				default:
					break;
				}
				return true;
			}
		});
		
		imgRight.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					v.setPressed(true);
					if (isActive) {
						client.send("3");
					}
					break;
					
				case MotionEvent.ACTION_UP:
					v.setPressed(false);
					if (isActive) {
						client.send("0");
					}
					break;

				default:
					break;
				}
				return true;
			}
		});
		imgLost.setOnClickListener(this);

	}

	@Override
	public void onResume() {
		super.onResume();
		
		client.connect();

		laserClient.connect();

		onStartStream();
		
//		SocketClient.getInstances().setInfo("abc", "123123", robot);
//		SocketClient.getInstances().connect(new Notification() {
//			
//			@Override
//			public void notifyMessage(String message) {
//				// TODO Auto-generated method stub
//				Log.v(TAG, "Message "+message);
//				//Toast.makeText(LoginActivity.this, message, 1000).show();
//				if(activity != null){
//					if(mLoadingDialog != null && mLoadingDialog.isShowing()){
//						mLoadingDialog.dismiss();
//					}
//					
//					if(message.contains("norobot")){
//						isShowAlert = true;
//						activity.runOnUiThread(new Runnable() {
//							
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								showAlert("No Robot", "There is no robot with this name");
//							}
//						});
//					}
//				}
//			}
//			
//			@Override
//			public void notifyError() {
//				// TODO Auto-generated method stub
//				if(activity != null){
//					if(mLoadingDialog != null && mLoadingDialog.isShowing()){
//						mLoadingDialog.dismiss();
//					}
//					
//				}
//			}
//			
//			@Override
//			public void notifyDisconnected() {
//				// TODO Auto-generated method stub
//				Log.e(TAG, "notifyDisconnected");
//				
//				if(activity != null){
//					if(mLoadingDialog != null && mLoadingDialog.isShowing()){
//						mLoadingDialog.dismiss();
//					}
//					
//					if(!isShowAlert){
//						activity.runOnUiThread(new Runnable() {
//							
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								isShowAlert = true;
//								showAlert("Timeout", "Your session is ended. System will disconnect now. Please help us spread this remote to the world");
//							}
//						});
//					}
//				}
//			}
//			
//			@Override
//			public void notifyConnected() {
//				// TODO Auto-generated method stub
//				Log.i(TAG, "Connected");
//				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
//					mLoadingDialog.dismiss();
//					
//					runOnUiThread(new Runnable() {
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							showSuccess();
//						}
//					});
//				}
//			}
//		});
		
//		if (SocketClient.getInstances().getClient() == null) {
//			isActive = false;
//		} else {
//			isActive = true;
//			client = SocketClient.getInstances().getClient();
//		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.but_right:
			if (isActive) {
				client.send("r");
			}
			break;

		case R.id.img_state_connect:
//			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//			intent.putExtra("robot", robot);
//			startActivityForResult(intent, 100);
			break;
			
		case R.id.laser1:
			if (isActive) {
				client.send("x");
			}
			break;
			
		case R.id.laser3:
			if (isActive) {
				client.send("z");
			}
			break;
			
		case R.id.laser2:
			if (isActive) {
				client.send("y");
			}
			break;
		
		default:
			break;
		}
	}
	
	Handler handler = new Handler();
	Runnable r = new Runnable() {
		public void run() {
			Log.v(TAG, "Disconnect");
			client.disconnect();
			imgLost.setBackgroundResource(R.drawable.icon_lost);
			imgLost.setClickable(true);
			showAlertTimout("Timeout", "Your session is ended. System will disconnect now. Please help us spread this remote to the world");
		}
	};

    //handler.postDelayed(r, 5000);

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			if (resultCode == RESULT_OK) {
				String user = data.getStringExtra("user");
				String pass = data.getStringExtra("pass");

				Log.e(TAG, "ALo User pass " + user + " " + pass);

				imgLost.setBackgroundResource(R.drawable.icon_connected);
				imgLost.setClickable(false);
				
				showAlert("Done", getResources().getString(R.string.welcom_license));
				
				handler.postDelayed(r, 300000);
			}
		}
	}// onActi

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.but_backward:
			
			break;
			
		case R.id.but_forward:
			
			break;
			
		case R.id.but_left:
			
			break;
			
		case R.id.but_right:
			
			break;
		case R.id.laser_beam:
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void notifyMessage(String message) {
		// TODO Auto-generated method stub
		Log.v(TAG, "notifyMessage "+message);
	}

	@Override
	public void notifyError() {
		// TODO Auto-generated method stub
		Log.v(TAG, "notifyError ");
	}

	@Override
	public void notifyConnected() {
		// TODO Auto-generated method stub
		Log.v(TAG, "notifyConnected ");
	}

	@Override
	public void notifyDisconnected() {
		// TODO Auto-generated method stub
		Log.v(TAG, "notifyDisconnected ");
	}

	
	private void showAlert(String title, String message) {
		Log.e(TAG, "showAlert");
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ControlActivity.this);
		alertDialogBuilder.setCancelable(true);
		alertDialogBuilder.setTitle(title);
		if(!message.equals("")){
			alertDialogBuilder.setMessage(message);
		}
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
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
//		alertDialog.show();
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
						//findViewById(R.id.but_center).setBackgroundResource(R.drawable.icon_connected_tokyo);
						imgLost.setBackgroundResource(R.drawable.icon_connected);
						imgLost.setClickable(false);
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
//		alertDialog.show();
	}
	
	private void showAlertTimout(String title, String message) {
		Log.e(TAG, "showAlertTimout");
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ControlActivity.this);
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
//		.setNegativeButton("No", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				// if this button is clicked, just close
//				// the dialog box and do nothing
//				dialog.cancel();
//			}
//		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
//		alertDialog.show();
	}
}
