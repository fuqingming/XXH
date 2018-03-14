package com.jy.xxh.bean.response;

/**
 * Created by HH
 * Date: 2017/12/7
 */

public class ResponseFollowBean extends ResponseBaseBean {
    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public class Content{
        private int info;

        public int getInfo() {
            return info;
        }

        public void setInfo(int info) {
            this.info = info;
        }
    }
}
