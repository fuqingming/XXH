package com.jy.xxh.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public abstract class FragmentActivityBase extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		init();
		setContentView(setLayoutResourceId());
		setUpView();
		setUpData();
	}

	protected abstract int setLayoutResourceId();

	protected void init(){

	}

	protected abstract void setUpView();

	protected void setUpData(){}

	protected void startActivityWithoutExtras(Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
	}

	protected void startActivityWithExtras(Class<?> clazz, Bundle extras) {
		Intent intent = new Intent(this, clazz);
		intent.putExtras(extras);
		startActivity(intent);
	}
	
    @Override  
    public Resources getResources() {
        Resources res = super.getResources();    
        Configuration config = new Configuration();    
        config.setToDefaults();    
        res.updateConfiguration(config,res.getDisplayMetrics() );  
        return res;  
    }   
}
