package com.jy.xxh.bean.response;

import com.jy.xxh.bean.base.HallLiveTypeBean;
import com.jy.xxh.bean.base.RoomBean;
import com.jy.xxh.bean.base.VideoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HH
 * Date: 2017/12/7
 */

public class ResponseHallLiveTypeBean extends ResponseBaseBean {
    private List<HallLiveTypeBean> content;

    public List<HallLiveTypeBean> getContent() {
        return content;
    }

    public void setContent(List<HallLiveTypeBean> content) {
        this.content = content;
    }
}
