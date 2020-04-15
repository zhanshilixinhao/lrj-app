package com.lrj.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;


public class jiGuangOauthLoginPost {
    /**
     * @Description  极光推送专用post
     * @Author PrinceCharmingDong
     * @Date 2020/3/4
     */
    public static String doPostForJpush (String JSONBody) {

       /* OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        //请求参数
             JSONObject json = new JSONObject();
             json.put("loginToken", JSONBody);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),String.valueOf(json));
        Request request = new Request.Builder()
                .url("https://api.verification.jpush.cn/v1/web/loginTokenVerify ")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Basic ZWFmZmZkNjhkNWEzMGI3NmVhOGEyZWJmOjI2ODUyOTZlYjc2NDM0ZDUwMjgzYzQ3Ng==")
                .build();
        Response response = client.newCall(request).execute();*/
  /*      //创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();*/
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        String responseContent = "";
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://api.verification.jpush.cn/v1/web/loginTokenVerify");
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Basic ZWFmZmZkNjhkNWEzMGI3NmVhOGEyZWJmOjI2ODUyOTZlYjc2NDM0ZDUwMjgzYzQ3Ng==");
            JSONObject json = new JSONObject();
            json.put("loginToken", JSONBody);
            httpPost.setEntity(new StringEntity(String.valueOf(json)));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            //HttpEntity entity = (HttpEntity) response.getEntity();
            //responseContent = EntityUtils.toString((org.apache.http.HttpEntity) entity, "UTF-8");
            System.out.println(responseContent);
            return responseContent;

        } catch (ClientProtocolException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            close(response, httpClient);
        }
        return null;
    }



    private static void close(Closeable... closeables) {
        if (closeables != null && closeables.length > 0) {
            try {
                for (Closeable closeable : closeables) {
                    if (closeable != null) {
                        closeable.close();
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args){
        String JSONBody = "";
        String result = doPostForJpush(JSONBody);
        System.out.println(result);
    }
}
