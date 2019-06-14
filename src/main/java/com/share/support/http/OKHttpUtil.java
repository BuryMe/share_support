package com.share.support.http;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * OKHttp 请求封装
 *
 * @author fuxuan
 * @date 2019/6/11 0011 18:31
 * @description
 */
@Component
public class OKHttpUtil {

    private static OkHttpClient okHttpClient;

    public static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            synchronized (OKHttpUtil.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .build();
                }
            }
        }
        return okHttpClient;
    }

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static class HttpClientBuilder {
        /**
         * 请求url
         */
        private String url;

        /**
         * 请求头
         */
        private Map<String, String> headers;

        /**
         * 请求参数
         */
        private Map<String, String> requests;

        /**
         * json请求参数
         */
        private String json;

        /**
         * 请求方法 get?post?put? 暂不支持delete 乀(ˉεˉ乀)
         */
        private String method;

        public HttpClientBuilder url(String url) {
            this.url = url;
            return this;
        }

        public HttpClientBuilder headers(Map<String, String> headers) {

            this.headers = headers;
            return this;
        }

        public HttpClientBuilder requests(Map<String, String> requests) {
            this.requests = requests;
            return this;
        }

        public HttpClientBuilder json(String json) {
            this.json = json;
            return this;
        }

        public HttpClientBuilder method(String method) {
            this.method = method;
            return this;
        }

        public HttpClientBuilder build() {
            return this;
        }
    }

    public static HttpClientBuilder create() {
        return new HttpClientBuilder();
    }


    //    ----------------------------- http 请求封装 ------------------------------------

    /**
     * 同步 http 请求
     *
     * @param builder
     * @return jsonString
     * @throws IOException
     */
    public static String synchronizeRequest(HttpClientBuilder builder) throws IOException {
        String url = builder.url;
        Map<String, String> headers = builder.headers;
        Map<String, String> requests = builder.requests;
        String json = builder.json;
        String method = builder.method;

        Headers.Builder headerBuilder = new Headers.Builder();
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach((k, v) -> headerBuilder.add(k, v));
        }

        FormBody.Builder formBuilder = new FormBody.Builder();
        if (!CollectionUtils.isEmpty(requests)) {
            requests.forEach((k, v) -> formBuilder.add(k, v));
        }

        RequestBody requestBody;
        if (StringUtils.isEmpty(json)) {
            requestBody = formBuilder.build();
        } else {
            requestBody = RequestBody.create(JSON, json);
        }

        Request.Builder requestBuilder = new Request.Builder();
        Request request;
        switch (method) {
            case "get":
                url = spliceUrl(url, requests);
                request = requestBuilder.url(url).headers(headerBuilder.build()).get().build();
                break;
            case "post":
                request = requestBuilder.url(url).headers(headerBuilder.build()).post(requestBody).build();
                break;
            case "put":
                url = spliceUrl(url, requests);
                request = requestBuilder.url(url).headers(headerBuilder.build()).put(requestBody).build();
                break;
            default:
                throw new IllegalArgumentException("Unexpected method value: " + method);
        }
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Http Communication failure:" + response);
        }
        return response.body().string();
    }

    private static String spliceUrl(String url, Map<String, String> params) {
        if (!CollectionUtils.isEmpty(params)) {
            StringBuffer stringBuffer = new StringBuffer(url);
            stringBuffer.append("?");
            params.forEach((k, v) -> stringBuffer.append(k).append("=").append(v).append("&"));
            return stringBuffer.toString();
        }
        return url;
    }


    /**
     * Http请求方法封装
     *
     * @param methodType       post :表示POST请求，get :表示GET请求 put:表示put请求
     * @param isJson           true json
     * @param url              请求url
     * @param paramMap         请求参数
     * @param isSynchronize    true:同步，false:y异步，默认为同步
     * @param headerMap        请求头封装map
     * @param responseCallback 异步请求处理线程
     */
    @Deprecated
    public static String request(String methodType, Boolean isJson, String url, Map<String, Object> paramMap, Map<String, String> headerMap,
                                 Boolean isSynchronize, Callback responseCallback) throws IOException, IllegalArgumentException {
//        header
        Headers.Builder headerBuilder = new Headers.Builder();
        if (!CollectionUtils.isEmpty(headerMap)) {
            headerMap.forEach((k, v) -> headerBuilder.add(k, v));
        }
//        requestBody
        RequestBody requestBody;
        if (Objects.equals(methodType, "post") && Objects.equals(isJson, true)) {
            requestBody = RequestBody.create(JSON, JSONObject.toJSONString(paramMap));
        } else {
            FormBody.Builder formBuilder = new FormBody.Builder();
            paramMap.forEach((k, v) -> formBuilder.add(k, (String) v));
            requestBody = formBuilder.build();
        }
//        request
        Request.Builder requestBuilder = new Request.Builder();
        Request request;
        if (Objects.equals(methodType, "post")) {
            request = requestBuilder.url(url).headers(headerBuilder.build()).post(requestBody).build();
        } else if (Objects.equals(methodType, "get")) {
            request = requestBuilder.url(url).headers(headerBuilder.build()).build();
        } else if (Objects.equals(methodType, "put")) {
            request = requestBuilder.url(url).headers(headerBuilder.build()).put(requestBody).build();
        } else {
            throw new IllegalArgumentException("methodType error:" + methodType);
        }
//        response
        Response response;
        if (Objects.equals(isSynchronize, true)) {
            response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Http Communication failure:" + response);
            }
            return response.body().string();
        } else {
            if (Objects.isNull(responseCallback)) {
                throw new IllegalArgumentException("responseCallback can not null");
            }
            okHttpClient.newCall(request).enqueue(responseCallback);
            return "";
        }
    }


}
