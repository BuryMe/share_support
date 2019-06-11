package com.share.support.http;

import com.google.common.collect.Maps;
import com.squareup.okhttp.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * OK http
 *
 * @author fuxuan
 * @date 2019/5/29 0029 14:57
 * @description
 */
public class HttpClient {

    private static final OkHttpClient okHttpClient = new OkHttpClient();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private static String SPOST = "SPosT";

    private static String SGET = "SGet";

    private static String APOST = "APost";

    private static String AGET = "AGet";


    /**
     * 同步 Http请求方法封装
     *
     * @param requestType       SPost 表示POST请求，SGet 表示GET请求
     * @param url               请求url
     * @param requestJsonString 请求参数JSONString，如果为GET请求则传null.
     * @param headerMap         请求头封装map
     * @return
     * @throws IOException
     */
    public static String synchronizeRequest(String requestType, String url, String requestJsonString, Map<String, String> headerMap) throws IOException {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (!CollectionUtils.isEmpty(headerMap)) {
            headerMap.forEach((k, v) -> headerBuilder.add(k, v));
        }
        Request.Builder requestBuilder = new Request.Builder();
        Request request;
        if (Objects.equals(requestType, SPOST)) {
            request = requestBuilder.url(url).headers(headerBuilder.build()).post(RequestBody.create(JSON, requestJsonString)).build();
        } else if (Objects.equals(requestType, SGET)) {
            request = requestBuilder.url(url).headers(headerBuilder.build()).build();
        } else {
            throw new IOException("requestType error:" + requestType);
        }
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Http Communication failure:" + response);
        }
        return response.body().toString();
    }

    /**
     * 异步http请求封装
     *
     * @param requestType       APost 表示POST请求，AGet 表示GET请求
     * @param url               请求url
     * @param requestJsonString 请求参数JSONString，如果为GET请求则传null.
     * @param headerMap         请求头封装map
     * @param responseCallback  response处理线程
     * @throws IOException
     */
    public static void asynchronousRequest(String requestType, String url, String requestJsonString, Map<String, String> headerMap, Callback responseCallback) throws IOException {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (!CollectionUtils.isEmpty(headerMap)) {
            headerMap.forEach((k, v) -> headerBuilder.add(k, v));
        }
        Request.Builder requestBuilder = new Request.Builder();
        Request request;
        if (Objects.equals(requestType, APOST)) {
            request = requestBuilder.url(url).post(RequestBody.create(JSON, requestJsonString)).headers(headerBuilder.build()).build();
        } else if (Objects.equals(requestType, AGET)) {
            request = requestBuilder.url(url).headers(headerBuilder.build()).build();
        } else {
            throw new IOException("requestType error:" + requestType);
        }
        okHttpClient.newCall(request).enqueue(responseCallback);
    }


    public static void main(String[] args) throws IOException {

    }
}
