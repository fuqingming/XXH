package com.jy.xxh.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.baidu.mobstat.StatService;
import com.jy.xxh.backhandler.BackHandlerHelper;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.util.HUDProgressUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.ButterKnife;

/**
 * Created by HH
 * Date: 2017/11/9
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    protected KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(setLayoutResourceId());
        setUpView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        kProgressHUD = new HUDProgressUtils().showLoadingImage(this);
        setUpData();
    }

    @Override
    protected void onResume() {
        if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        StatService.onResume(this);

        super.onResume();
    }

    @Override
    protected void onPause() {

        StatService.onPause(this);

        super.onPause();
    }

    protected abstract int setLayoutResourceId();

    protected void init(){
        HttpClient.init(getApplicationContext(),false);
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
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
