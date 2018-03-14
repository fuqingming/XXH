package com.jy.xxh.util;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by HH
 * Date: 2017/11/23
 */

public class HUDProgressUtils {
    /**
     * 菊花加载
     *
     * @param context
     * @return
     */
    public KProgressHUD showLoadingImage(Context context) {

        return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setCancellable(false);
    }

    public KProgressHUD showMessage(Context context, String message){
        return KProgressHUD.create(context, KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel(message)
                .setCancellable(true)
                .setAutoDismiss(true)
                .show();
    }
}
