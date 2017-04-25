package control.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.uudam.license2draw.R;


public class LoginActivity extends FragmentActivity implements OnClickListener {

	private static final String TAG = "LoginActivity";
	
	private EditText user, pass;
	private ImageView imgLogin, imgRegister;
	private String robot;
	ProgressDialog mLoadingDialog;
	private boolean isShowAlert = false;
	
	private Activity activity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getActionBar().hide();
		robot = getIntent().getExtras().getString("robot");
		
		user = (EditText)findViewById(R.id.edt_id);
		pass = (EditText)findViewById(R.id.edt_pass);
		
		imgLogin = (ImageView)findViewById(R.id.img_login);
		imgRegister = (ImageView)findViewById(R.id.img_register);
		imgLogin.setOnClickListener(this);
		imgRegister.setOnClickListener(this);
		
		activity = this;
	}
	
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_login:
			mLoadingDialog = new ProgressDialog(LoginActivity.this);
			mLoadingDialog.setMessage("Loading...");
			mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mLoadingDialog.setCancelable(false);
			mLoadingDialog.show();
			SocketClient.getInstances().setInfo(user.getText().toString().trim(), pass.getText().toString().trim(), robot);
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
					}
					Intent returnIntent = new Intent();
					returnIntent.putExtra("user", user.getText().toString().trim());
					returnIntent.putExtra("pass", pass.getText().toString().trim());
					setResult(RESULT_OK, returnIntent);
					finish();
				}
			});
			
			break;
			
		case R.id.img_register:
			startActivityForResult(new Intent(getApplicationContext(),
					RegisterActivity.class), 100);
			//startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
			
			break;

		default:
			hideSoftKeyboard(this);
			break;
		}
	}
	
	private void showAlert(String title, String message) {
		Log.e(TAG, "showAlert");
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
		alertDialogBuilder.setCancelable(true);
		alertDialogBuilder.setTitle(title);
		if(!message.equals("")){
			alertDialogBuilder.setMessage(message);
		}
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				isShowAlert = false;
			}
		});
		
		if(title.equals("Timeout")){
			alertDialogBuilder.setCancelable(false).setPositiveButton("Not now", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					isShowAlert = false;
				}
			});
		}
//		.setNegativeButton("No", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				// if this button is clicked, just close
//				// the dialog box and do nothing
//				dialog.cancel();
//			}
//		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			if (resultCode == RESULT_OK) {
				String usertxt = data.getStringExtra("user");
				String passtxt = data.getStringExtra("pass");

				Log.e(TAG, "ALo User pass " + user + " " + pass);
				user.setText(usertxt);
				pass.setText(passtxt);
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}// onActi

}
