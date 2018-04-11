package com.jy.xxh.bean.base;

import java.io.Serializable;

/**
 * Created by HH
 * Date: 2017/11/20
 */

public class ChatLiveMessageBean implements Serializable {
    public static final String user_char = "user_char";         //用户文字消息
    public static final String user_pic = "user_pic";           //用户图片消息,
    public static final String teacher_char = "teacher_char";   //老师文字消息
    public static final String teacher_pic = "teacher_pic";     //老师图片消息
    public static final String teacher_rep = "teacher_rep";     //老师回复消息

    private String c_messageType;   //消息类型,共5种类型 'user_char':用户文字消息 'user_pic':用户图片消息, 'teacher_char':老师文字消息, 'teacher_pic':老师图片消息, 'teacher_rep':老师回复消息
    private String c_st_content;    //用户发言消息
    private long c_time_linux;    //发言时间(时间戳)
    private String c_t_content;     //老师发言内容
    private String nic_name;        //发言人的名称
    private String user_photo;      //发言人头像
    private String c_replay_name;   //回复用户名
    private String c_st_id;

    public ChatLiveMessageBean(String c_messageType, String c_st_content, long c_time_linux, String c_t_content, String nic_name, String user_photo, String c_replay_name,String c_st_id) {
        this.c_messageType = c_messageType;
        this.c_st_content = c_st_content;
        this.c_time_linux = c_time_linux;
        this.c_t_content = c_t_content;
        this.nic_name = nic_name;
        this.user_photo = user_photo;
        this.c_replay_name = c_replay_name;
        this.c_st_id = c_st_id;
    }

    public String getC_messageType() {
        return c_messageType;
    }

    public void setC_messageType(String c_messageType) {
        this.c_messageType = c_messageType;
    }

    public String getC_st_content() {
        return c_st_content;
    }

    public void setC_st_content(String c_st_content) {
        this.c_st_content = c_st_content;
    }

    public long getC_time_linux() {
        return c_time_linux;
    }

    public void setC_time_linux(long c_time_linux) {
        this.c_time_linux = c_time_linux;
    }

    public String getC_t_content() {
        return c_t_content;
    }

    public void setC_t_content(String c_t_content) {
        this.c_t_content = c_t_content;
    }

    public String getNic_name() {
        return nic_name;
    }

    public void setNic_name(String nic_name) {
        this.nic_name = nic_name;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getC_replay_name() {
        return c_replay_name;
    }

    public void setC_replay_name(String c_replay_name) {
        this.c_replay_name = c_replay_name;
    }

    public String getC_st_id() {
        return c_st_id;
    }

    public void setC_st_id(String c_st_id) {
        this.c_st_id = c_st_id;
    }
}
