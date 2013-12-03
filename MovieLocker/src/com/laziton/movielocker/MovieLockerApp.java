package com.laziton.movielocker;

import android.app.Application;
import android.content.Context;

public class MovieLockerApp extends Application {
	public static Context context;
	
	@Override
	public void onCreate(){
		super.onCreate();
		context = this.getApplicationContext();
	}
}
