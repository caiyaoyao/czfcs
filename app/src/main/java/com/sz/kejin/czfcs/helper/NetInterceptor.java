package com.sz.kejin.czfcs.helper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 时间：2019-07-23 09:10
 * 说明：
 */
public class NetInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder().addHeader("Connection", "close").build();
        return chain.proceed(request);
    }
}
