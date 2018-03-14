package com.jy.xxh.http;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by HH
 * Date: 2017/11/23
 */

public interface ApiService {
    @GET()
    Observable<ResponseBody> executeGet(@Url String url);
}
