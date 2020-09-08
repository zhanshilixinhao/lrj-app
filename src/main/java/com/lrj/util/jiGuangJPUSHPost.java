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


public class jiGuangJPUSHPost {
    /**
     * @Description  极光推送助手端专用POST
     * @Author PrinceCharmingDong
     * @Date 2020/3/4
     */
    public static String doPostForHelpStaff (String JSONBody) {

        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        String responseContent = "";
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://api.jpush.cn/v3/push");
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Basic MGNhNWZlYWE5YzU4Y2IyZGI4Yzk4YjhkOjhjNWY3Zjc2NmNlYWQzNmUzODk0ZGE5NQ==");
            JSONObject json = new JSONObject();
            json.put("platform", "android");
            json.put("audience", "all");
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

   /* public static void main(String[] args){
        String JSONBody = "";
        String result = doPostForJpush(JSONBody);
        System.out.println(result);
    }*/
}
