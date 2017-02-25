package control.activity;

import android.app.Application;

public class MyApplication extends Application {
	private static MyApplication singleton;
	
	public static MyApplication getInstance(){
		if(singleton == null){
			singleton = new MyApplication();
		}
		return singleton;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
	}
}
