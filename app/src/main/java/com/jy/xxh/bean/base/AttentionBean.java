package com.jy.xxh.bean.base;

import java.io.Serializable;

/**
 * Created by HH
 * Date: 2017/11/20
 */

public class AttentionBean implements Serializable {

    private String t_id;
    private String t_name;
    private String t_photo;
    private String t_brief;
    private String a_id_attention;

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

    public String getA_id_attention() {
        return a_id_attention;
    }

    public void setA_id_attention(String a_id_attention) {
        this.a_id_attention = a_id_attention;
    }
}
