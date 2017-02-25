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

import org.rtspplayer.sample.R;

import control.socket.WebSocketClient;


public class TokyoActivity  extends Activity implements OnClickListener {

	private static final String TAG  = "TokyoActivity";
	private WebSocketClient client;
	private boolean isActive = false;
	private ImageView imgLeftTop, imgTop, imgRightTop;
	private ImageView imgLeft, imgRight;
	private ImageView imgLeftDown, imgDown, imgRightDown;
	private ImageView imgLost;
	
	private String robot;
	private ProgressDialog mLoadingDialog;
	private Activity activity;
	private boolean isShowAlert = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tokyo);
		
		Log.e(TAG, "onCreate "+0);
		
		mLoadingDialog = new ProgressDialog(TokyoActivity.this);
		mLoadingDialog.setMessage("Loading...");
		mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mLoadingDialog.setCancelable(false);
		mLoadingDialog.show();
		
		activity = this;
		robot = getIntent().getExtras().getString("robot");
		
		imgDown = (ImageView)findViewById(R.id.but_down);
		imgLeftDown = (ImageView)findViewById(R.id.but_down_left);
		imgRightDown = (ImageView)findViewById(R.id.but_down_right);
		
		imgTop = (ImageView)findViewById(R.id.but_top);
		imgLeftTop = (ImageView)findViewById(R.id.but_left_top);
		imgRightTop = (ImageView)findViewById(R.id.but_right_top);
		
		imgLeft = (ImageView)findViewById(R.id.but_left);
		imgRight = (ImageView)findViewById(R.id.but_right);
		
		imgLost = (ImageView)findViewById(R.id.but_center);
		
		imgLost.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						imgLost.setBackgroundResource(R.drawable.laser_beam_pressed);
						v.setPressed(true);
						if (isActive) {
							client.send("lz");
						} 
						break;
					case MotionEvent.ACTION_UP:
						imgLost.setBackgroundResource(R.drawable.laser_beam);
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
		
		
		imgDown.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					imgDown.setBackgroundResource(R.drawable.icon_down_pressed);
					v.setPressed(true);
					if (isActive) {
						client.send("8");
					}
					break;
					
				case MotionEvent.ACTION_UP:
					v.setPressed(false);
					imgDown.setBackgroundResource(R.drawable.icon_down);
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
		
		imgLeftDown.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					imgLeftDown.setBackgroundResource(R.drawable.right_rotate_pressed);
					v.setPressed(true);
					if (isActive) {
						client.send("9");
					}
					break;
					
				case MotionEvent.ACTION_UP:
					imgLeftDown.setBackgroundResource(R.drawable.right_rotate);
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
		
		imgRightDown.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					imgRightDown.setBackgroundResource(R.drawable.left_rotate_pressed);
					v.setPressed(true);
					if (isActive) {
						client.send("7");
					}
					break;
					
				case MotionEvent.ACTION_UP:
					v.setPressed(false);
					imgRightDown.setBackgroundResource(R.drawable.left_rotate);
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
		
		imgTop.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					imgTop.setBackgroundResource(R.drawable.icon_up_pressed);
					v.setPressed(true);
					if (isActive) {
						client.send("2");
					}
					break;
					
				case MotionEvent.ACTION_UP:
					v.setPressed(false);
					imgTop.setBackgroundResource(R.drawable.icon_up);
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
		
		imgLeftTop.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					imgLeftTop.setBackgroundResource(R.drawable.icon_up_left_pressed);
					v.setPressed(true);
					if (isActive) {
						client.send("1");
					}
					break;
					
				case MotionEvent.ACTION_UP:
					imgLeftTop.setBackgroundResource(R.drawable.icon_up_left);
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
		
		imgRightTop.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					imgRightTop.setBackgroundResource(R.drawable.icon_up_right_pressed);
					v.setPressed(true);
					if (isActive) {
						client.send("3");
					}
					break;
					
				case MotionEvent.ACTION_UP:
					imgRightTop.setBackgroundResource(R.drawable.icon_up_right);
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
					imgLeft.setBackgroundResource(R.drawable.icon_left_tokyo_pressed);
					v.setPressed(true);
					if (isActive) {
						client.send("4");
					}
					break;
					
				case MotionEvent.ACTION_UP:
					imgLeft.setBackgroundResource(R.drawable.icon_left_tokyo);
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
		
		imgRight.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					imgRight.setBackgroundResource(R.drawable.icon_right_tokyo_pressed);
					v.setPressed(true);
					if (isActive) {
						client.send("6");
					}
					break;
					
				case MotionEvent.ACTION_UP:
					imgRight.setBackgroundResource(R.drawable.icon_right_tokyo);
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
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
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
	}
	
	public void clickLeftSide(View v){
		Log.e(TAG, "clickLeftSide "+0);
		if (isActive) {
			client.send("z");
		}
	}

	public void clickRightSquare(View v){
		Log.e(TAG, "clickRightSquare "+0);
		if (isActive) {
			client.send("y");
		}
	}
	
	public void clickRightCircle(View v){
		Log.e(TAG, "clickRightCircle "+0);
		if (isActive) {
			client.send("x");
		}
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
			
		case R.id.but_tokyo:
			intent.putExtra("robot", "3000");
			startActivity(intent);
			break;
			
		case R.id.but_saigon:
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
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TokyoActivity.this);
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
						findViewById(R.id.but_center).setBackgroundResource(R.drawable.icon_connected);
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
	private void showAlertTimout(String title, String message) {
		Log.e(TAG, "showAlertTimout");
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TokyoActivity.this);
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
