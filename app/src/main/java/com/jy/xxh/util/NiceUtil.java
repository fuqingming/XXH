package com.jy.xxh.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jy.xxh.R;
import com.jy.xxh.backhandler.OnTaskSuccessComplete;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by XiaoJianjun on 2017/5/8.
 * 工具类.
 */
public class NiceUtil {
    /**
     * Get activity from context object
     *
     * @param context something
     * @return object of Activity or null if it is not Activity
     */
    public static Activity scanForActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * Get AppCompatActivity from context
     *
     * @param context
     * @return AppCompatActivity if it's not null
     */
    private static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) return null;
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    @SuppressLint("RestrictedApi")
    public static void showActionBar(Context context) {
        ActionBar ab = getAppCompActivity(context).getSupportActionBar();
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false);
            ab.show();
        }
        scanForActivity(context)
                .getWindow()
                .clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @SuppressLint("RestrictedApi")
    public static void hideActionBar(Context context) {
        ActionBar ab = getAppCompActivity(context).getSupportActionBar();
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false);
            ab.hide();
        }
        scanForActivity(context)
                .getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return width of the screen.
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return heiht of the screen.
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal   dp value
     * @return px value
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 将毫秒数格式化为"##:##"的时间
     *
     * @param milliseconds 毫秒数
     * @return ##:##
     */
    public static String formatTime(long milliseconds) {
        if (milliseconds <= 0 || milliseconds >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        long totalSeconds = milliseconds / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static boolean isWiFiActive(Context context)
    {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);//得到wifi管理器对象

        return wifimanager.isWifiEnabled();//返回wifi状态
    }

    /**
     * 判断网络情况
     * @param context 上下文
     * @return false 表示没有网络 true 表示有网络
     */
    public static boolean isNetworkAvalible(Context context) {
        // 获得网络状态管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 建立网络数组
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();

            if (net_info != null) {
                for (int i = 0; i < net_info.length; i++) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 打开或关闭wifi
     */
    public static void isOpenWifi(Context context,boolean isOpen)
    {
        WifiManager wifimanager  = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);//得到wifi管理器对象
        wifimanager.setWifiEnabled(isOpen); //打开或关闭
    }

    public static Dialog showDialogOpenWifi(final Context context,final OnTaskSuccessComplete onTaskSuccess)
    {
        View.OnClickListener onLeftButtonClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (onTaskSuccess != null)
                {
                    onTaskSuccess.onSuccess(null);
                }
            }
        };

        View.OnClickListener onRightButtonClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NiceUtil.isOpenWifi(context,true);
            }
        };

        return showCommonDialog(context,
                "打开wifi",
                "确认打开wifi吗?",
                "取消",
                onLeftButtonClickListener,
                "确定",
                onRightButtonClickListener);
    }

    @SuppressLint("NewApi")
    public static Dialog showCommonDialog(final Context context,
                                          final String strTitle,
                                          final String strMsg,
                                          final String strLeftButtonText,
                                          final View.OnClickListener onLeftButtonClickListener,
                                          final String strRightButtonText,
                                          final View.OnClickListener onRightButtonClickListener)
    {
        View vContent = LayoutInflater.from(context).inflate(R.layout.dialog_common, null);
        final Dialog dlg = new Dialog(context, R.style.common_dialog);
        dlg.setContentView(vContent);
        dlg.setCanceledOnTouchOutside(false); // 点击窗口外区域不消失
        dlg.setCancelable(false); // 返回键不消失

        // 必须用代码调整dialog的大小
        WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
        //lp.width = (int) (MyApplication.s_nScreenWidth * 0.95);
        //lp.height = (int) (MyApplication.m_nScreenHeight * 0.5);
        lp.width = (int) context.getResources().getDimension(R.dimen.dialog_width);
        dlg.getWindow().setAttributes(lp);

        // title
        TextView tvTitle = vContent.findViewById(R.id.tv_title);
        if (strTitle != null && !strTitle.isEmpty())
        {
            tvTitle.setText(strTitle);
        }
        else
        {
            tvTitle.setVisibility(View.GONE);
        }

        // msg
        TextView tvMsg =  vContent.findViewById(R.id.tv_msg);
        tvMsg.setText(strMsg);

        // left button
        Button btnLeft =  vContent.findViewById(R.id.btn_left);
        btnLeft.setText(strLeftButtonText);
        btnLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dlg.dismiss();
                if (onLeftButtonClickListener != null)
                {
                    onLeftButtonClickListener.onClick(v);
                }
            }
        });

        // right button
        Button btnRight = vContent.findViewById(R.id.btn_right);
        btnRight.setText(strRightButtonText);
        btnRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dlg.dismiss();
                if (onRightButtonClickListener != null)
                {
                    onRightButtonClickListener.onClick(v);
                }
            }
        });

        dlg.show();

        return dlg;
    }
}
