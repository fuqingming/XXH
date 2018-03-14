package com.jy.xxh.bean.response;

/**
 * Created by HH
 * Date: 2017/11/9
 */

public class ResponseBaseBean {
    private Integer code;
    private Boolean result;
    private String message;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
