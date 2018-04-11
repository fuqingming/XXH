package com.jy.xxh.bean.base;

import java.io.Serializable;

/**
 * Created by HH
 * Date: 2017/11/20
 */

public class VideoBean implements Serializable {

    private String v_id;
    private String v_name;
    private String img_url;
    private String video_url;
    private String v_type;
    private String v_time;
    private String room_id;
    private String timeD;
    private String t_v_id;
    private String t_nic_name;
    private String t_photo;

    public String getV_id() {
        return v_id;
    }

    public void setV_id(String v_id) {
        this.v_id = v_id;
    }

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getV_type() {
        return v_type;
    }

    public void setV_type(String v_type) {
        this.v_type = v_type;
    }

    public String getV_time() {
        return v_time;
    }

    public void setV_time(String v_time) {
        this.v_time = v_time;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getTimeD() {
        return timeD;
    }

    public void setTimeD(String timeD) {
        this.timeD = timeD;
    }

    public String getT_v_id() {
        return t_v_id;
    }

    public void setT_v_id(String t_v_id) {
        this.t_v_id = t_v_id;
    }

    public String getT_nic_name() {
        return t_nic_name;
    }

    public void setT_nic_name(String t_nic_name) {
        this.t_nic_name = t_nic_name;
    }

    public String getT_photo() {
        return t_photo;
    }

    public void setT_photo(String t_photo) {
        this.t_photo = t_photo;
    }
}
