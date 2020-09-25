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

/**
 * @author : cwj
 * @describe : 极光 单条模板短信发送
 * @date : 2020-9-23
 */
public class jiGuangSMSSend {
    public static String sendSMS (String mobile,Integer signId,Integer tempId,String temp_para) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        String responseContent = "";
        try {
            JSONObject json = new JSONObject();
            json.put("mobile", mobile);
            json.put("sign_id", signId);
            json.put("temp_id", tempId);
            if(temp_para !=""){
                json.put("temp_para", temp_para);
            }
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://api.sms.jpush.cn/v1/messages");
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.addHeader("Authorization", "Basic MGNhNWZlYWE5YzU4Y2IyZGI4Yzk4YjhkOjhjNWY3Zjc2NmNlYWQzNmUzODk0ZGE5NQ==");

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
}
