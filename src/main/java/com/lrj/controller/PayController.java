package com.lrj.controller;

import com.lrj.VO.AliPayVo;
import com.lrj.VO.OrderVo;
import com.lrj.VO.ResultVo;
import com.lrj.config.PayConfig;
import com.lrj.service.IOrderService;
import com.lrj.service.IPayService;

import com.lrj.util.WXPayUtil;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.*;

/**
 * @author : cwj
 * @describe : 支付方式
 * @date : 2020-4-9
 */
@RestController
public class PayController {

    private static Object lock = new Object();
    @Resource
    private IPayService payService;
    @Resource
    private IOrderService orderService;


    /**
     * 微信支付 统一下单 生成预支付交易单
     */
    @RequestMapping(value = "/weixinPay", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultVo appWXPayNotify(HttpServletRequest request) throws Exception {

        /**接收前端传回参数**/
        String orderId = request.getParameter("orderId");
        String userId = request.getParameter("userId");

        //查询该交易订单
        OrderVo orderVo = orderService.findOrderByOrderId(orderId);
        /** 校验必须参数 **/
        if (orderId == null || userId == null) {
            return new ResultVo("success", 1, "参数有误,请检查参数", null);
        }
        /**调用接口必须参数**/
        String url = PayConfig.UNIFIEDORDER;
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("app_id", PayConfig.getApp_appID());
        params.put("mch_id", PayConfig.getApp_mchID());
        params.put("nonce_str", String.valueOf(new Date().getTime()));
        params.put("body", "懒人家洗衣-订单充值");
        params.put("out_trade_no", orderId);
        //计算充值金额
        double amount = 0D;
        try {
            BigDecimal bg = new BigDecimal(String.valueOf(orderVo.getTotalPrice()));
            amount = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        } catch (Exception e) {
            new ResultVo("success", 1, "充值金额的格式不正确", null);
        }
        params.put("total_fee", 10);
        InetAddress localAddr = InetAddress.getLocalHost();
        String localIp = localAddr.getHostAddress().toString();
        params.put("spbill_create_ip", localIp); //终端IP
        params.put("notify_url", "http://192.168.0.115/MyWXPayNotifyUrl");  //异步通知回调地址
        params.put("trade_type", "APP"); //支付类型
        //将非空参数进行签名运算(排序，MD5加密)
        String sign = WXPayUtil.getSign(params, PayConfig.getApp_key());
        params.put("sign", sign);
        //将请求参数转化为微信支付要求的xml格式文件
        String xml = WXPayUtil.mapToXml(params);

        //调用对方demo请求下单
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        String responseContent = "";
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(xml));
            response = httpClient.execute(httpPost);
            HttpEntity entity = (HttpEntity) response.getEntity();
            responseContent = EntityUtils.toString((org.apache.http.HttpEntity) entity, "UTF-8");
            System.out.println(responseContent);
            response.close();
            httpClient.close();

        } catch (ClientProtocolException e) {
            System.out.println(e);
            new ResultVo("success", 1, "网络请求异常，请稍后重试或联系客服！", null);
        } catch (IOException e) {
            System.out.println(e);
            new ResultVo("success", 1, "网络请求异常，请稍后重试或联系客服！", null);
        } finally {
            close(response, httpClient);
        }
        if (responseContent != null) {
            //将String转化成map
            Map resultMap = WXPayUtil.xmlToMap(String.valueOf(responseContent));
            System.out.println("微信支付请求结果：" + resultMap);
            //请求返回结果的处理
            String returnCode = (String) resultMap.get("return_code");
            if (returnCode.equals("SUCCESS")) {
                String appid = (String) resultMap.get("appid");
                String mch_id = (String)resultMap.get("mch_id");
                String nonce_str = (String)resultMap.get("nonce_str");
                String resultSign = (String)resultMap.get("sign");
                String resultCode = (String) resultMap.get("result_Code");
                if(resultCode.equals("SUCCESS")){
                    List returnList = new ArrayList();
                    String trade_type = (String)resultMap.get("trade_type");
                    String prepay_id = (String)resultMap.get("prepay_id");
                    returnList.add(prepay_id);
                    return new ResultVo("success", 0, "预支付成功", returnList);
                }
            }else {
                new ResultVo("success", 1, (String) resultMap.get("return_msg"), null);
            }
        } else {
            new ResultVo("success", 1, "请求支付网关异常，请稍后重试或联系客服", null);
        }
        return null;
    }

    @RequestMapping(value = "/MyWXPayNotifyUrl",method = {RequestMethod.GET,RequestMethod.POST})
    public void MyWXPayNotifyUrl(HttpServletRequest request){
        synchronized (lock) {
            Map xml = request.getParameterMap();
        }
    }

    /**
     * 支付宝 支付方式
     */
    @RequestMapping(value = "/AliPay",method = {RequestMethod.GET,RequestMethod.POST})
    public String appAliPayNotify(AliPayVo aliPayVo, Map parameterMap) throws Exception {
        return payService.appAliPayNotify(aliPayVo,parameterMap);
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
