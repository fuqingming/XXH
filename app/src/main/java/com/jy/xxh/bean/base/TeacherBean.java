package com.jy.xxh.bean.base;

import java.io.Serializable;

/**
 * Created by HH
 * Date: 2017/11/20
 */

public class TeacherBean implements Serializable {

    private String t_id;
    private String t_name;
    private String t_photo;
    private String t_brief;
    private String t_strategy;
    private String t_room;
    private String t_count;
    private String t_join_count;
    private int a_id_attention;

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public String getT_photo() {
        return t_photo;
    }

    public void setT_photo(String t_photo) {
        this.t_photo = t_photo;
    }

    public String getT_brief() {
        return t_brief;
    }

    public void setT_brief(String t_brief) {
        this.t_brief = t_brief;
    }

    public String getT_strategy() {
        return t_strategy;
    }

    public void setT_strategy(String t_strategy) {
        this.t_strategy = t_strategy;
    }

    public String getT_room() {
        return t_room;
    }

    public void setT_room(String t_room) {
        this.t_room = t_room;
    }

    public String getT_count() {
        return t_count;
    }

    public void setT_count(String t_count) {
        this.t_count = t_count;
    }

    public String getT_join_count() {
        return t_join_count;
    }

    public void setT_join_count(String t_join_count) {
        this.t_join_count = t_join_count;
    }

    public int getA_id_attention() {
        return a_id_attention;
    }

    public void setA_id_attention(int a_id_attention) {
        this.a_id_attention = a_id_attention;
    }
}
