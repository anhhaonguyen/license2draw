package control.activity;

import android.app.ProgressDialog;
import android.util.Log;


import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import control.socket.WebSocketClient;

public class SocketClient {
	
	private static final String TAG = "SocketClient";
	
	private static SocketClient sClient;
	
	private WebSocketClient client;
	private String user, pass, port;
	ProgressDialog mLoadingDialog;
	
	public static SocketClient getInstances(){
		if(sClient == null){
			sClient = new SocketClient();
		}
		
		return sClient;
	}
	
	public void setInfo(String user, String pass, String port){
		this.user = user;
		this.pass = pass;
		this.port = port;
	}
	
	public WebSocketClient getClient(){
		return client;
	}
	
	public void disconnect(){
		client.disconnect();
	}
	
	public void connect(final Notification notifyListener){
		
		List<BasicNameValuePair> extraHeaders = Arrays
				.asList(new BasicNameValuePair("send-token", user+","+pass+","+1));//"Abc,123123"

//		if(port.equals("5003")){
//			port = "3005";
//		}
		client = new WebSocketClient(
				URI.create("ws://139.162.47.39:"+port),//ws://128.199.223.211:9000//ws://139.162.47.39
				new WebSocketClient.Listener() {
					@Override
					public void onConnect() {
						Log.d(TAG, "Connected! "+port);
//						if(port.equals("3005")){
//							client.send("send-token,"+user+","+pass);
//						}else{
							client.send("send-token,"+user+","+pass+","+1);
//						}
					}

					@Override
					public void onMessage(String message) {
						Log.d(TAG, String.format("Got string message! %s",
								message));
						
						if(message.equals("success")){
							
							notifyListener.notifyConnected();
						}else{
							notifyListener.notifyMessage(message);
						}
						//client.send("Rau Ma");
					}

					@Override
					public void onMessage(byte[] data) {
						Log.d(TAG,
								String.format("Got binary message! %s", data));
					}

					@Override
					public void onDisconnect(int code, String reason) {
						Log.d(TAG, String.format(
								"Disconnected! Code: %d Reason: %s", code,
								reason));
						notifyListener.notifyDisconnected();
					}

					@Override
					public void onError(Exception error) {
						Log.e(TAG, "Error!", error);
						notifyListener.notifyError();
					}

				}, extraHeaders);

		client.connect();
	}

//	public SocketClient(){
//		
//	}
}
