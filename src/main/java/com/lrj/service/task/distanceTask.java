package com.lrj.service.task;

import lombok.AllArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author cwj
 * @descrip: 定时查询两个坐标点之间的距离
 * @date 2020/5/28 0028下午 2:03
 */
@Component
@EnableScheduling
@AllArgsConstructor
@RestController
public class distanceTask {

    /**
     * 主动查询是否可以 点击按钮
     * @param origins
     * @param destination
     * @param staffType
     * @return
     */
    @RequestMapping(value = "/isCanOnClick",method = {RequestMethod.GET,RequestMethod.POST})
    public Boolean isCanOnClick(String origins,String destination,Integer staffType,Integer reservationId){
        /** 校验必须参数 **/
        if (staffType == null || staffType == 0 || reservationId==null || reservationId == 0) {
            return false;
        }
        String key = "81b5d90e4a546330f9e0672877299bc0"; //此key 为高德地图提供，使用API接口必传的key
        String url = "https://restapi.amap.com/v3/distance"; //距离测量API url
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        String responseContent = "";
        httpClient= HttpClients.createDefault();
        //创建URLBuilder
        URIBuilder uriBuilder= null;
        try {
            uriBuilder = new URIBuilder(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //设置参数
        uriBuilder.setParameter("origins",origins).setParameter("destination",destination).setParameter("key",key);
        HttpGet httpGet= null;
        try {
            httpGet = new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity = response.getEntity();
            response = httpClient.execute(httpGet);
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        JSONObject jsonObject = JSONObject.fromObject(responseContent);
        //获取距离
        Integer distance = Integer.parseInt(JSONObject.fromObject(JSONArray.fromObject(jsonObject.get("results")).get(0)).getString("distance"));
        switch (staffType){
            case 1:
                if (distance <= 20){
                    return true;
                }else {
                    return false;
                }
            case 3:
                if(distance <= 50){
                    return true;
                }else {
                    return false;
                }
            default:
                    return false;
        }
    }
}

