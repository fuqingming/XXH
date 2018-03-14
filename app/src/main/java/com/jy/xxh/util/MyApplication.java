package com.jy.xxh.util;

import android.app.Application;
import android.util.Log;

/**
 * @author 付庆明
 *
 */
public class MyApplication extends Application 
{
	private static final String LOG_TAG = "MyApplication";
	
	/** 标记是 测试环境(true) 还是 正式环境(false) */
	public static final Boolean TEST_ENVIRONMENT = true; 
	
	private static MyApplication s_instance = null;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		Log.d(LOG_TAG, "onCreate()");

		s_instance = this;
	}
	
	public static MyApplication getInstance()
	{
		return s_instance;
	}
	
	public void exit(){
		try{
			Thread.sleep(100);
		}
		catch (InterruptedException e){
			e.printStackTrace();
		}
		
		System.exit(0);
	}	
}
