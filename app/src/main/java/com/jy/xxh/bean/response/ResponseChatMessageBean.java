package com.jy.xxh.bean.response;

import com.jy.xxh.bean.base.ChatMessageBean;

import java.util.List;

/**
 * Created by HH
 * Date: 2017/12/7
 */

public class ResponseChatMessageBean extends ResponseBaseBean {
    public List<ChatMessageBean> content;

    public List<ChatMessageBean> getContent() {
        return content;
    }

    public void setContent(List<ChatMessageBean> content) {
        this.content = content;
    }
}
