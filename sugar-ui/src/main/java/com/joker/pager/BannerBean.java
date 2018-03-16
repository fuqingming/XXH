package com.joker.pager;

import java.io.Serializable;

/**
 * Created by HH
 * Date: 2017/11/20
 */

public class BannerBean implements Serializable {

    private String b_id;
    private String b_link;
    private String b_turn_link;

    public BannerBean(String b_id, String b_link, String b_turn_link) {
        this.b_id = b_id;
        this.b_link = b_link;
        this.b_turn_link = b_turn_link;
    }

    public String getB_id() {
        return b_id;
    }

    public void setB_id(String b_id) {
        this.b_id = b_id;
    }

    public String getB_link() {
        return b_link;
    }

    public void setB_link(String b_link) {
        this.b_link = b_link;
    }

    public String getB_turn_link() {
        return b_turn_link;
    }

    public void setB_turn_link(String b_turn_link) {
        this.b_turn_link = b_turn_link;
    }
}
