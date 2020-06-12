package com.lrj.util;

import okhttp3.*;

import java.io.IOException;

/**
 * @author yichenshanren
 * @date 2017/12/10
 */

public class OkHttpUtil {

    private OkHttpClient okHttpClient = null;

    /**
     * 请求的content-type 目前支持 application/x-www-form-urlencoded
     */
    public static final MediaType urlencoded
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    /**
     * okhttp get同步请求
     *
     * @param url   请求url
     * @param param 请求参数
     */
    public static Response get(String url, RequestParams param) throws IOException {
        // 如果有请求参数，拼接
        if (param != null) {
            url = url + param.getUrlParams();
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        return OkHttpManager.create().newCall(request).execute();
    }

    /**
     * okhttp get同步请求
     *
     * @param url   请求url
     * @param param 请求参数
     */
    public static Response get(OkHttpClient client, String url, RequestParams param) throws IOException {
        // 如果有请求参数，拼接
        if (param != null) {
            url = String.format("%s?%s", url, param.getUrlParams());
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        return client.newCall(request).execute();
    }

    /**
     * okhttp 同步请求
     *
     * @param urlString 请求url
     * @param params    请求参数字典
     */
    public static Response post(String urlString, RequestParams params) throws IOException {
        String content = "";
        if (params != null) {
            content = params.getUrlParams();
        }
        return OkHttpManager.create().newCall(new Request.Builder()
                .url(urlString)
                .post(RequestBody.create(urlencoded, content))
                .build()).execute();
    }

    /**
     * okhttp 同步请求,需提供OkHttpClient
     *
     * @param urlString 请求url
     * @param params    请求参数字典
     */
    public static Response post(OkHttpClient client, String urlString, RequestParams params) throws IOException {
        String content = "";
        if (params != null) {
            content = params.getUrlParams();
        }
        return client.newCall(new Request.Builder()
                .url(urlString)
                .post(RequestBody.create(urlencoded, content))
                .build()).execute();
    }

    /**
     * okhttp 同步请求,需提供OkHttpClient
     *
     * @param urlString 请求url
     * @param params    请求参数字典
     */
    public static Response postJson(OkHttpClient client, String urlString, RequestParams params) throws IOException {
        String content = "";
        if (params != null) {
            content = params.getJSON();
        }
        return client.newCall(new Request.Builder()
                .url(urlString)
                .post(RequestBody.create(MediaType.parse("application/json;charset=utf-8"), content))
                .build()).execute();
    }

}
