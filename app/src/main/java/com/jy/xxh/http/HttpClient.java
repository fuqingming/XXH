package com.jy.xxh.http;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.tamic.novate.Novate;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HH
 * Date: 2017/11/22
 */

public class HttpClient {

    @SuppressLint("StaticFieldLeak")
    private static Novate mNovate = null;

    public static void init(Context context,boolean reInit) {
        if (reInit){
            mNovate = null;
        }
        if (mNovate == null) {
            Context appliactionContext;
            try {
                Activity activity = (Activity) context;
                appliactionContext = activity.getApplicationContext();
            } catch (Exception e) {
                appliactionContext = context;
            }

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request.Builder requestBuilder = originalRequest.newBuilder()
                            .header("AppType", "TPOS")
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .method(originalRequest.method(), originalRequest.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            };
            builder.addInterceptor(headerInterceptor);

            builder.connectTimeout(15, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);

            OkHttpClient okHttpClient = builder.build();

            String baseUrl = "http://xxh.qimengzhibo.com/";

            mNovate = new Novate.Builder(appliactionContext)
                    .addCache(false)
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .build();
        }
    }

    public static Novate novateClient() {
        if (mNovate == null) {
            Log.e("Novate", "Novate is null!");
        }
        return mNovate;
    }

    public static <T> void  get(String url, HttpCallback<T> httpCallback){
        ApiService apiService = mNovate.create(ApiService.class);
        mNovate.call(apiService.executeGet(url),httpCallback);
    }

    public static <T> void get(String url, Map<String,Object> params,HttpCallback<T> httpCallback){
        mNovate.rxGet(url,params,httpCallback);
    }

    public static <T> void post(String url,Map<String,Object> params,HttpCallback<T> httpCallback){
        mNovate.rxBody(url,params,httpCallback);

    }

}
