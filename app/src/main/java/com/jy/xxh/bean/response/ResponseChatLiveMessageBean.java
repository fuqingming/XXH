package com.jy.xxh.bean.response;

import com.jy.xxh.bean.base.ChatLiveMessageBean;
import com.jy.xxh.bean.base.ChatMessageBean;

import java.util.List;

/**
 * Created by HH
 * Date: 2017/12/7
 */

public class ResponseChatLiveMessageBean extends ResponseBaseBean {
    public List<ChatLiveMessageBean> content;

    public List<ChatLiveMessageBean> getContent() {
        return content;
    }

    public void setContent(List<ChatLiveMessageBean> content) {
        this.content = content;
    }
}
