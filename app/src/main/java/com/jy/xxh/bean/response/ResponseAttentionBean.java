package com.jy.xxh.bean.response;

import com.jy.xxh.bean.base.AttentionBean;

import java.util.List;

/**
 * Created by HH
 * Date: 2017/12/7
 */

public class ResponseAttentionBean extends ResponseBaseBean {
    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public class Content{
        private List<AttentionBean> info;

        public List<AttentionBean> getInfo() {
            return info;
        }

        public void setInfo(List<AttentionBean> info) {
            this.info = info;
        }
    }
}
