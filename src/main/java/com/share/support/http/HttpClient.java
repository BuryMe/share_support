package com.share.support.http;

import com.squareup.okhttp.*;
import com.sun.javafx.iio.ios.IosDescriptor;

import java.io.IOException;
import java.util.Map;

/**
 * OK http
 *
 * @author fuxuan
 * @date 2019/5/29 0029 14:57
 * @description
 */
public class HttpClient {

    private static final OkHttpClient okHttpClient = new OkHttpClient();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 同步 发送post请求
     *
     * @param url
     * @param json json数据
     * @return
     * @throws IOException
     */
    public static String synchronizePost(String url, String json) throws IOException {
        Request request = new Request.Builder().url(url).post(RequestBody.create(JSON, json)).build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return response.body().string();
    }

    /**
     * 异步 发送post请求
     *
     * @param url
     * @param json             json数据
     * @param responseCallback 回调处理线程
     * @throws IOException
     */
    public static void asynchronousPost(String url, String json, Callback responseCallback) throws IOException {
        Request request = new Request.Builder().url(url).post(RequestBody.create(JSON, json)).build();
        okHttpClient.newCall(request).enqueue(responseCallback);
    }
}
