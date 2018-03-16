package com.jy.xxh.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.hyphenate.easeui.ui.EaseChatRoomListener;

/**
 * Created by asus on 2018/3/15.
 */

public abstract class ChatRoomListener extends EaseChatRoomListener {

    protected abstract String setRoomID();
    protected abstract Context setContext();
    final Activity activity = (Activity) setContext();

    @Override
    public void onChatRoomDestroyed(final String roomId, final String roomName) {

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (roomId.equals(setRoomID())) {
                    Toast.makeText(setContext(), com.hyphenate.easeui.R.string.the_current_chat_room_destroyed, Toast.LENGTH_LONG).show();

                    if (activity != null && !activity.isFinishing()) {
                        activity.finish();
                    }
                }
            }
        });
    }

    @Override
    public void onRemovedFromChatRoom(final String roomId, final String roomName, final String participant) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (roomId.equals(setRoomID())) {
                    Toast.makeText(activity, com.hyphenate.easeui.R.string.quiting_the_chat_room, Toast.LENGTH_LONG).show();
                    if (activity != null && !activity.isFinishing()) {
                        activity.finish();
                    }
                }
            }
        });
    }

    @Override
    public void onMemberJoined(final String roomId, final String participant) {
        if (roomId.equals(setRoomID())) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
//						Toast.makeText(getActivity(), "member join:" + participant, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onMemberExited(final String roomId, final String roomName, final String participant) {
        if (roomId.equals(setRoomID())) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
//						Toast.makeText(getActivity(), "member exit:" + participant, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
