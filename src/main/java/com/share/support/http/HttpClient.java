package com.share.support.http;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.logging.log4j.util.Strings;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    private static String POST = "post";

    private static String GET = "get";

    private static String FORMTYPE = "form";

    private static String JSONTYPE = "json";

    public static final String token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJDSEFOTkVMX0FVVEhFTlRJQ0FUSU9OIiwiQVBQX0lEIjoiNDI3MTgwMjczNCIsImlzcyI6IkFVVEhfU0VSVkVSIiwiZXhwIjoxNTI0Nzk2MTM3LCJpYXQiOjE1MjQ3MDk3MzcsIkNIQU5ORUxfQVBQX05BTUUiOiLmna3lt57mn6Llm7rph5Hono3mnI3liqHlpJbljIXmnInpmZDlhazlj7giLCJqdGkiOiJiY2I0ZGUxOC1iODQxLTQ1NjYtYjUwMS05MmJmZmIyMTY4OGIifQ.H-k6nQwWoVS4-BLKnDmlIt4DizMhA7p5L3uR7RhEwd-G6uykYIMo9KoxFIQ6abTa6GV2hjynf2e4j13x5YesDoiFObGEaIPEQHiHWfYX-1iEthWJIMp9z_UkQUg6F0r8jk1I_a5RM1GYz8ODz0140ktv1a-i97mGVvZSTIoZI2Y";

    /**
     * uloan demo:
     * "appId": "e47b479b6fd54faab01468f840935164", "appSecret": "@5zgPxkZ72mfe3#y",
     * channelID: ul_llzf
     * POST /api/channel/query/channel-token
     * Demo环境：https://auth-demo.dianrong.com/auth-server
     * 生产环境：https://auth.dianrong.com/auth-server
     * <p>
     */


    /**
     * Http请求方法封装
     *
     * @param methodType       post :表示POST请求，get :表示GET请求
     * @param requestType      form :表单提交 json :json提交
     * @param url              请求url
     * @param paramMap         请求参数
     * @param isSynchronize    true:同步，false:y异步，默认为同步
     * @param headerMap        请求头封装map
     * @param responseCallback 异步请求处理线程
     * @return
     * @throws IOException
     */
    public static String request(String methodType, String requestType, String url, Map<String, Object> paramMap, Map<String, String> headerMap,
                                 Boolean isSynchronize, Callback responseCallback) throws IOException {
//        header
        Headers.Builder headerBuilder = new Headers.Builder();
        if (!CollectionUtils.isEmpty(headerMap)) {
            headerMap.forEach((k, v) -> headerBuilder.add(k, v));
        }
//        requestBody
        RequestBody requestBody;
        if (Objects.equals(requestType, FORMTYPE)) {
            FormBody.Builder formBuilder = new FormBody.Builder();
            paramMap.forEach((k, v) -> formBuilder.add(k, (String) v));
            requestBody = formBuilder.build();
        } else if (Objects.equals(requestType, JSONTYPE)) {
            requestBody = RequestBody.create(JSON, JSONObject.toJSONString(paramMap));
        } else {
            throw new IOException("requestType error:" + requestType);
        }
//        request
        Request.Builder requestBuilder = new Request.Builder();
        Request request;
        if (Objects.equals(methodType, POST)) {
            request = requestBuilder.url(url).headers(headerBuilder.build()).post(requestBody).build();
        } else if (Objects.equals(methodType, GET)) {
            request = requestBuilder.url(url).headers(headerBuilder.build()).build();
        } else {
            throw new IOException("methodType error:" + methodType);
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
                throw new IOException("responseCallback can not null");
            }
            okHttpClient.newCall(request).enqueue(responseCallback);
            return Strings.EMPTY;
        }
    }


    public static void main(String[] args) throws IOException {
//        模拟异步请求第三方接口
//        Map<String,Object> paramMap = new HashMap<>();
//        paramMap.put("mobile","18888888888");
//        paramMap.put("clientSourceType","BD");
//        paramMap.put("type","SPEEDLOAN");
//        Map<String,String> headerMap = new HashMap<>();
//        headerMap.put("X-Channel-Authorization",token);
//        String res = HttpClient.synchronizeRequest("SPost",
//            "https://api-demo.dianrong.com/gw/borrow-channel/v3/channels/fqgj/registration?mobile=188888888&clientSourceType=BD&type=SPEEDLOAN",
//        JSONObject.toJSONString(paramMap),headerMap);
//        System.out.println(res);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("appId", "e47b479b6fd54faab01468f840935164");
        paramMap.put("appSecret", "@5zgPxkZ72mfe3#y");
        String res = HttpClient.request("post", "form",
        "https://auth-demo.dianrong.com/auth-server/api/channel/query/channel-token",
        paramMap, null, true, null);
        System.out.println(res);
        Map<String, Object> result = JSONObject.parseObject(res, Map.class);
        System.out.println("111");


    }
}
