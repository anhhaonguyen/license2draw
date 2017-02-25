package control.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.rtspplayer.sample.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends Activity implements OnClickListener {

	private static final String TAG = "LoginActivity";
	
	private EditText user, pass, confirmPass;
	private ImageView imgRegister;
	private ProgressDialog mProgressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		user = (EditText)findViewById(R.id.edt_id);
		pass = (EditText)findViewById(R.id.edt_pass);
		confirmPass = (EditText)findViewById(R.id.edt_confirm_pass);
		
		imgRegister = (ImageView)findViewById(R.id.img_register);
		imgRegister.setOnClickListener(this);
		//imgLogin.setOnClickListener(this);
	}
	
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_register:
			new RegisterAsyn().execute();
			break;

		default:
			hideSoftKeyboard(this);
			break;
		}
	}
	
	private class RegisterAsyn extends AsyncTask<String, Void, JSONObject> {
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            mProgressDialog = new ProgressDialog(RegisterActivity.this);
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
        }

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub

			String username = user.getText().toString().trim();
			String passW = pass.getText().toString().trim();
			
			JSONObject jSon = getPostData(username, passW, "http://188.166.225.139:9000/user/create.json");
			Log.i(TAG, "JSON RESPONSE "+jSon.toString());
			return jSon;
		}
		
		@Override
		protected void onPostExecute(JSONObject obj) {
			mProgressDialog.dismiss();
			
			if(obj == null){
				dialog("ERROR", "");
			}else{
				
				String value = obj.optString("message");
				if(value.equals("true")){
					Log.i(TAG, "true");
					Intent returnIntent = new Intent();
					returnIntent.putExtra("user", user.getText().toString().trim());
					returnIntent.putExtra("pass", pass.getText().toString().trim());
					setResult(RESULT_OK, returnIntent);
					finish();
				}else{
					Log.e(TAG, "false");
					dialog("Register fail","Please register again");
				}
			}
		}
	}
	
	private void dialog(String title, String mess){
		AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle(title);
        
        if(!mess.equals("")){
        	builder.setMessage(mess);
        }
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            	
            }
        });
        builder.show(); //To show the AlertDialog
	}
	
	public static JSONObject getPostData(String username, String pass,
										 String api) {
		Log.e(TAG, "getPostData " + api);

		InputStream is = null;
		JSONObject jObj = null;
		String jsonString = null;
		
		HttpClient httpClient = new DefaultHttpClient();
        // Creating HTTP Post
        HttpPost httpPost = new HttpPost(api);
        
//        httpPost.addHeader("Content-Type", "application/json");
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("username", username));
        nameValuePair.add(new BasicNameValuePair("password",pass));
 
        // Url Encoding the POST parameters
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // writing error to Log
            e.printStackTrace();
        }
 
        // Making HTTP Request
        try {
            HttpResponse response = httpClient.execute(httpPost);
 
            // writing response to log
            Log.d("Http Response:", " OK "+response.getStatusLine().getStatusCode());
            
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Log.e(TAG, "200 ");
				HttpEntity httpEntity = response.getEntity();
				is = httpEntity.getContent();
			} 
        } catch (ClientProtocolException e) {
            // writing exception to log
            e.printStackTrace();
        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
 
        }
        
        try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			jsonString = sb.toString();

			Log.v(TAG, "jSOn " + api + "\n" + jsonString);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		try {
			Object json = new JSONTokener(jsonString).nextValue();
			if (json instanceof JSONObject) {
				// you have an object - try to parse the string to a JSON Object
				jObj = new JSONObject(jsonString);
			}
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		return jObj;

	}
}
