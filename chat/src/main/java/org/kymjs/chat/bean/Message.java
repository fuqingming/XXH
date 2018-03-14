/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kymjs.chat.bean;

import java.util.Date;

/**
 * 聊天消息javabean
 *
 * @author kymjs (http://www.kymjs.com/)
 */
public class Message {
    public final static int MSG_TYPE_TEXT = 3;
    public final static int MSG_TYPE_PHOTO = 1;
    public final static int MSG_TYPE_FACE = 2;      //图片发送失败或者初始化显示图片

    public final static int MSG_STATE_SENDING = 3;  //发送中
    public final static int MSG_STATE_SUCCESS = 1;  //成功
    public final static int MSG_STATE_FAIL = 2;     //失败

    public final static String MSG_OTHER_MEMBER = "otherMember";
    public final static String MSG_MYSELF = "myself";
    public final static String MSG_TEACHER = "teacher";

    private int type; // 0-text | 1-photo | 2-face | more type ...
    private int state; // 0-sending | 1-success | 2-fail //消息发送的状态
    private String userName;
    private String userIcon;
    private String content;             //发送文字内容
    private Boolean sendSucces;         //是否发送成功
    private String mumberType;
    private String time;
    private String strStuText;             //老师回复谁的文本
    private String strStuName;             //老师回复谁的昵称

    public Message(int type, int state, String userName,
                   String userIcon,
                   String content,String strStuName,String strStuText, Boolean sendSucces,String mumberType, String time,String strFromId) {
        super();
        this.type = type;
        this.state = state;
        this.userName = userName;
        this.userIcon = userIcon;
        this.content = content;
        this.sendSucces = sendSucces;
        this.mumberType = mumberType;
        this.time = time;
        this.strStuName = strStuName;
        this.strStuText = strStuText;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSendSucces() {
        return sendSucces;
    }

    public void setSendSucces(Boolean sendSucces) {
        this.sendSucces = sendSucces;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMumberType() {
        return mumberType;
    }

    public void setMumberType(String mumberType) {
        this.mumberType = mumberType;
    }

    public String getStrStuText() {
        return strStuText;
    }

    public void setStrStuText(String strStuText) {
        this.strStuText = strStuText;
    }

    public String getStrStuName() {
        return strStuName;
    }

    public void setStrStuName(String strStuName) {
        this.strStuName = strStuName;
    }
}
