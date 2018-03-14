package com.jy.xxh.bean.response;

/**
 * Created by HH
 * Date: 2017/12/7
 */

public class ResponseChatBean extends ResponseBaseBean {
    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public class Content{
        private Info info;

        public Info getInfo() {
            return info;
        }

        public void setInfo(Info info) {
            this.info = info;
        }

        public class Info{
            private int a_id_attention;

            public int getA_id_attention() {
                return a_id_attention;
            }

            public void setA_id_attention(int a_id_attention) {
                this.a_id_attention = a_id_attention;
            }
        }
    }
}
