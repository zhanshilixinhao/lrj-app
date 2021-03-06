package com.lrj.util;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
     public static String doPostForHelpStaff () {

        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        String responseContent = "";
        try {
            JSONObject json = new JSONObject();
            json.put("platform", "android");
            JSONObject audienceJSON = new JSONObject();
            JSONArray tagJSONArray = new JSONArray();
            tagJSONArray.add("企业用户");
            audienceJSON.put("tag", tagJSONArray);
            json.put("audience", audienceJSON);
            JSONObject notificationJSON = new JSONObject();
            notificationJSON.put("alert", "您有一条新服务订单，请查看!");
            json.put("notification", notificationJSON);
            httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost("https://api.jpush.cn/v3/push");
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.addHeader("Authorization", "Basic YWQ0ZGVjOTU0NmM5NDU1OTlkMWFiMDBlOjgwM2Q4OGNlMmM1NjRlMWZiYTE5MGEwOA==");

            httpPost.setEntity(new StringEntity(json.toString(),"UTF-8"));
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
