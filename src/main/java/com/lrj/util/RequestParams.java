package com.lrj.util;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yichenshanren @date 2017/10/27.
 */

public class RequestParams {

    private HashMap<String, Object> params;

    public RequestParams() {
        params = new LinkedHashMap<>();
    }

    public Object put(String key, Object value) {
        return params.put(key, value);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * 获取请求的参数url encode
     *
     * @return 请求参数
     */
    public String getUrlParams() {
        if (isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        int i = 0;
        try {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (i > 0) {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
                i++;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public boolean isEmpty() {
        if (params != null && !params.isEmpty()) {
            return false;
        }
        return true;
    }

    public String getJSON() {
        return JSON.toJSONString(params);
    }
}
