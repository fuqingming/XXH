package com.jy.xxh.huanxin.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jy.xxh.huanxin.utils.EMNotificationManager;

/**
 * Created by zhangsong on 17-9-15.
 */
public class EMFCMMSGService extends FirebaseMessagingService {
    private static final String TAG = "EMFCMMSGService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            String message = remoteMessage.getData().get("alert");
            Log.i(TAG, "onMessageReceived: " + message);
            EMNotificationManager.getInstance(this).sendNotification(message);
        }
    }
}
