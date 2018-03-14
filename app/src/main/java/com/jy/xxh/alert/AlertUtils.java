package com.jy.xxh.alert;

import android.app.Dialog;
import android.content.Context;

import com.jy.xxh.R;

/**
 * Created by HH
 * Date: 2017/11/23
 */

public class AlertUtils {


    public static void MessageAlertShow(Context context, String title, String message){
        new Alert(context, R.style.dialog, message, new Alert.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                dialog.dismiss();
            }
        }).setTitle(title).setPositiveButton(null).show();
    }

    public static void MessageAlertSelect(Context context, String title, String message, Alert.OnCloseListener onCloseListener){
        new Alert(context, R.style.dialog, message, onCloseListener).setTitle(title).show();
    }

}
