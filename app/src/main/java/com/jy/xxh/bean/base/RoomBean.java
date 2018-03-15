package com.jy.xxh.bean.base;

import java.io.Serializable;

/**
 * Created by HH
 * Date: 2017/11/20
 */

public class RoomBean implements Serializable {

    public static  final int isLocked = 1;
    public static final int isOnline = 1;

    private String t_nic_name;
    private String t_id;
    private String r_id;
    private int r_is_screte;
    private int r_t_online;
    private String r_people;
    private String r_icon;
    private String r_room_id;
    private String r_room_breif;
    private String r_pwd;
    private String t_photo;

    public String getT_nic_name() {
        return t_nic_name;
    }

    public void setT_nic_name(String t_nic_name) {
        this.t_nic_name = t_nic_name;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getT_photo() {
        return t_photo;
    }

    public void setT_photo(String t_photo) {
        this.t_photo = t_photo;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public int getR_is_screte() {
        return r_is_screte;
    }

    public void setR_is_screte(int r_is_screte) {
        this.r_is_screte = r_is_screte;
    }

    public int getR_t_online() {
        return r_t_online;
    }

    public void setR_t_online(int r_t_online) {
        this.r_t_online = r_t_online;
    }

    public String getR_people() {
        return r_people;
    }

    public void setR_people(String r_people) {
        this.r_people = r_people;
    }

    public String getR_icon() {
        return r_icon;
    }

    public void setR_icon(String r_icon) {
        this.r_icon = r_icon;
    }

    public String getR_room_id() {
        return r_room_id;
    }

    public void setR_room_id(String r_room_id) {
        this.r_room_id = r_room_id;
    }

    public String getR_room_breif() {
        return r_room_breif;
    }

    public void setR_room_breif(String r_room_breif) {
        this.r_room_breif = r_room_breif;
    }

    public String getR_pwd() {
        return r_pwd;
    }

    public void setR_pwd(String r_pwd) {
        this.r_pwd = r_pwd;
    }


}
