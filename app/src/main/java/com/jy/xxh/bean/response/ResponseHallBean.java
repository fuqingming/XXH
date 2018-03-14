package com.jy.xxh.bean.response;

import com.joker.pager.BannerBean;
import com.jy.xxh.bean.base.RoomBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HH
 * Date: 2017/12/7
 */

public class ResponseHallBean extends ResponseBaseBean {
    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public class Content{
        private List<BannerBean> banner = new ArrayList<>();
        private List<RoomBean> room = new ArrayList<>();

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public List<RoomBean> getRoom() {
            return room;
        }

        public void setRoom(List<RoomBean> room) {
            this.room = room;
        }
    }
}
