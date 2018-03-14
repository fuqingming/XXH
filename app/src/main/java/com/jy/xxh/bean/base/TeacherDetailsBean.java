package com.jy.xxh.bean.base;

import java.io.Serializable;

/**
 * Created by HH
 * Date: 2017/11/20
 */

public class TeacherDetailsBean implements Serializable {

    private String title;
    private String text;

    public TeacherDetailsBean(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
