package com.jy.xxh.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.jy.xxh.R;

import org.greenrobot.eventbus.EventBus;
import org.kymjs.kjframe.KJActivity;

/**
 * Created by kongweile on 29/10/2016.
 * Auto hide and show navigation bar and status bar for API >= 19.
 * Keep screen on.
 */

public class VideoPlayerBaseActivity extends KJActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                      View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_chat_live);
    }
}
