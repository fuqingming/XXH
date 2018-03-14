package com.jy.xxh.http;

import com.google.gson.Gson;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.lang.reflect.ParameterizedType;

/**
 * Created by HH
 * Date: 2017/11/21
 */

public abstract class HttpCallback<T> extends RxStringCallback {

    @Override
    public void onStart(Object tag) {
        super.onStart(tag);
        OnRequestStart();
    }

    @Override
    public void onCompleted(Object tag) {
        super.onCompleted(tag);
        OnRequestFinish();
    }

    @Override
    public void onNext(Object tag, String response) {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        OnSuccess(transform(response,entityClass));
    }

    @Override
    public void onError(Object tag, Throwable e) {
        OnFailure(e.getMessage());
    }

    @Override
    public void onCancel(Object tag, Throwable e) {
        OnFailure(e.getMessage());
    }

    public abstract void OnSuccess(T response);

    public abstract void OnFailure(String message);

    public abstract void OnRequestStart();

    public abstract void OnRequestFinish();



    private T transform(String response, final Class<T> classOfT) throws ClassCastException {
        T dataResponse = null;
        try {
            if (response.charAt(0) == '{'){
                dataResponse = new Gson().fromJson(response, classOfT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataResponse;
    }
}
