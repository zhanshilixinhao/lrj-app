package com.lrj.util;

import com.alibaba.fastjson.JSON;
import com.lrj.VO.WXResult;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author yichenshanren
 * @date 2018/2/6
 */


public class WXCodeApi {

    //    private final static Logger log = LoggerFactory.getLogger(WXCodeApi.class);
    // app获取access_tpken
    public static final String URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    // 小程序获取session
    public static final String URL_M = "https://api.weixin.qq.com/sns/jscode2session";
    // app获取用户信息
    public static final String URL_USER_INFO = "https://api.weixin.qq.com/sns/userinfo";
    // 小程序公众号获取access_token
    public static final String MINI_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
    // 小程序
    private static final String APPID = "wxc31f3dd8ecfe95a8"; // wx4e47fc336f8578df
    private static final String APP_SECRET = "1596648e33b6142167e60545540666a6";

    // app
    private static final String APPID_APP = "wx6dbf2ef384cd6f34"; // wx4e47fc336f8578df
    private static final String APP_SECRET_APP = "2df2ceb6d7fe5d79b57428a4501ebc23";

    private static final OkHttpClient CLIENT = OkHttpManager.create(null, null);

    /**
     * 获取微信用户的信息
     *
     * @param accessToken 微信令牌
     * @param openId      微信用户唯一标识
     * @return
     * @author yichenshanren
     * @date 2018/2/6
     */
    public static WXResult getUserInfo(String accessToken, String openId) {
        RequestParams params = new RequestParams();
        params.put("access_token", accessToken);
        params.put("openid", openId);
        return wxRequest(URL_USER_INFO, params);
    }

    /**
     * 微信code换取openid
     *
     * @param code 微信code
     * @return
     * @author yichenshanren
     * @date 2018/2/6
     */
    public static WXResult getSession(Integer clientApp, String code) {
        String codeKey;
        String url;
        String appId;
        String secretKey;
        if (clientApp != 3) {
            codeKey = "code";
            url = URL;
            appId = APPID_APP;
            secretKey = APP_SECRET_APP;
        } else {
            codeKey = "js_code";
            url = URL_M;
            appId = APPID;
            secretKey = APP_SECRET;
        }

        RequestParams params = new RequestParams();
        params.put("appid", appId);
        params.put("secret", secretKey);
        params.put(codeKey, code);
        params.put("grant_type", "authorization_code");
        return wxRequest(url, params);
    }

    /**
     * 微信接口请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    private static WXResult wxRequest(String url, RequestParams params) {
        return wxRequest(url, params, 0);
    }

    /**
     * 微信接口请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    private static WXResult wxRequest(String url, RequestParams params, int method) {
        WXResult result = new WXResult();
        try {
            Response response;
            if (method == 0) {
                response = OkHttpUtil.get(CLIENT, url, params);
            } else {
                response = OkHttpUtil.postJson(CLIENT, url, params);
            }
            if (response.isSuccessful()) {
                String re = response.body().string();
//                log.info("微信换取openid:{}", re);
                result = JSON.parseObject(re, WXResult.class);
            } else {
                result.setErrcode(1);
                result.setErrmsg(response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.setErrcode(1);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }

    /**
     * 获取微信小城的access_token
     *
     * @return
     * @autho yichen
     * @date 2018/8/14
     */
    public static WXResult getAccessToken() {
        RequestParams params = new RequestParams();
        params.put("grant_type", "client_credential");
        params.put("appid", APPID);
        params.put("secret", APP_SECRET);
        return wxRequest(MINI_ACCESS_TOKEN, params);
    }

    public static void main(String[] args) {
        WXResult result = getAccessToken();
        int a = 1;
    }
}
