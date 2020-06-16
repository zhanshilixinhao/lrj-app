package com.lrj.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.lrj.VO.*;
import com.lrj.config.PayConfig;
import com.lrj.service.*;
import com.lrj.pojo.Balance;
import com.lrj.service.IBalanceService;
import com.lrj.service.IOrderService;
import com.lrj.service.IPayService;

import com.lrj.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    @Resource
    private IUserService userService;
    @Resource
    private TeamLaundryService teamLaundryService;
    @Resource
    private IBalanceService balanceService;

    private RebateService rebateService;

    public PayController(RebateService rebateService) {
        this.rebateService = rebateService;
    }

    /**
     * 用户充值 入口
     */
    @RequestMapping(value = "/recharge",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult recharge(Integer userId,Integer payType,HttpServletRequest request,Integer rechargeType) throws Exception{
        /** 校验必须参数 **/
        if (userId == null || userId == 0 || payType==null || payType==0 || rechargeType==null || rechargeType==0) {
            return new FormerResult("success", 1, "参数有误,请检查参数", null);
        }
        String orderNumber = "00000000000000";
        switch (rechargeType){
            //充值100 送20%余额
            case 1:
                if(payType == 1){
                    FormerResult formerResult = appWXPayNotify(request, orderNumber, 100.00);
                    return formerResult;
                    //支付宝支付
                }else if(payType == 2){
                    FormerResult formerResult = appAliPay(request, orderNumber, 100.00);
                    return formerResult;
                }
                break;
            //充值300 送25%余额
            case 2:
                if(payType == 1){
                    FormerResult formerResult = appWXPayNotify(request, orderNumber, 300.00);
                    return formerResult;
                    //支付宝支付
                }else if(payType == 2){
                    FormerResult formerResult = appAliPay(request, orderNumber, 300.00);
                    return formerResult;
                }
                break;
            //充值500 送30%余额
            case 3:
                if(payType == 1){
                    FormerResult formerResult = appWXPayNotify(request, orderNumber, 500.00);
                    return formerResult;
                    //支付宝支付
                }else if(payType == 2){
                    FormerResult formerResult = appAliPay(request, orderNumber, 500.00);
                    return formerResult;
                }
                break;
            //充值1000 送40%余额
            case 4:
                if(payType == 1){
                    FormerResult formerResult = appWXPayNotify(request, orderNumber, 1000.00);
                    return formerResult;
                    //支付宝支付
                }else if(payType == 2){
                    FormerResult formerResult = appAliPay(request, orderNumber, 1000.00);
                    return formerResult;
                }
                break;
            //自定义充值   不送
            case 5:
                BigDecimal payMoney = new BigDecimal(request.getParameter("payMoney"));
                if(payType == 1){
                    FormerResult formerResult = appWXPayNotify(request, orderNumber, payMoney.doubleValue());
                    return formerResult;
                    //支付宝支付
                }else if(payType == 2){
                    FormerResult formerResult = appAliPay(request, orderNumber, payMoney.doubleValue());
                    return formerResult;
                }
                break;
        }
        return null;
    }

    /**
     * 判断 支付方式
     */
    @RequestMapping(value = "/payForChoose", method = {RequestMethod.GET, RequestMethod.POST})
    public FormerResult payType(Integer payType, HttpServletRequest request) throws Exception {
        /**接收前端传回参数**/
        String orderNumber = request.getParameter("orderNumber");
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        Integer isBalance = Integer.parseInt(request.getParameter("isBalance"));

        /** 校验必须参数 **/
        if (orderNumber == null || userId == 0 || isBalance == null) {
            return new FormerResult("success", 1, "参数有误,请检查参数", null);
        }
        //查询该交易订单
        OrderVo orderVo = orderService.findOrderByOrderNumber(orderNumber);
        //是否使用余额支付
         //不使用
        if (isBalance == 0) {
            /** 校验必须字段 **/
            if (payType == null) {
                return new FormerResult("success", 1, "参数有误,请检查参数", null);
            //微信支付
            }else if(payType == 1){
                FormerResult formerResult = appWXPayNotify(request, orderNumber, 0.00);
                return formerResult;
            //支付宝支付
            }else if(payType == 2){
                FormerResult formerResult = appAliPay(request, orderNumber, 0.00);
                return formerResult;
            }
            return new FormerResult("success", 0, "请求支付完成", null);
            //使用
        } else if (isBalance == 1) {
            Balance balance = new Balance();
            balance.setUserId(2);
            balance.setBalance(new BigDecimal(100));
            //如果余额金额大于等于支付金额
            if (balance.getBalance().doubleValue() >= orderVo.getTotalPrice().doubleValue()) {
                //增加支付流水 记录
                Map<String, Object> flowRecordMap = new HashMap<String, Object>();
                flowRecordMap.put("trade_type", "1");
                flowRecordMap.put("bank_type", "");
                flowRecordMap.put("total_fee", orderVo.getTotalPrice().doubleValue());
                flowRecordMap.put("transaction_id", "");
                flowRecordMap.put("out_trade_no", orderVo.getOrderNumber());
                flowRecordMap.put("time_end", DateUtils.getNowDateTime());
                flowRecordMap.put("user_id", orderVo.getUserId());
                flowRecordMap.put("trade_source", "余额支付");
                payService.payFlowRecord(flowRecordMap);
                double balanceMoney = balance.getBalance().doubleValue() - orderVo.getTotalPrice().doubleValue();
                //修改余额
                balanceService.updateUserBalance(balanceMoney,userId);
                new FormerResult("success", 1, "请求支付完成", null);
            } else {
                double payMoney = orderVo.getTotalPrice().doubleValue() - balance.getBalance().doubleValue();
                //增加支付流水 记录
                Map<String, Object> flowRecordMap = new HashMap<String, Object>();
                flowRecordMap.put("trade_type", "2");
                flowRecordMap.put("bank_type", "");
                flowRecordMap.put("total_fee", balance.getBalance());
                flowRecordMap.put("cash_fee", "");
                flowRecordMap.put("transaction_id", "");
                flowRecordMap.put("out_trade_no", orderVo.getOrderNumber());
                flowRecordMap.put("time_end", DateUtils.getNowDateTime());
                flowRecordMap.put("user_id", orderVo.getUserId());
                flowRecordMap.put("trade_source", "部分余额支付");
                payService.payFlowRecord(flowRecordMap);
                //修改余额(余额清零)
                balanceService.updateUserBalance(0.00,userId);
                if(payType == 1){   //微信支付
                    appWXPayNotify(request, orderNumber, payMoney);
                }else if(payType == 2){   //支付宝支付
                    appAliPay(request, orderNumber, payMoney);
                }
               return new FormerResult("success", 0, "请求支付完成", null);
            }
            return new FormerResult("success", 0, "请求支付完成", null);
        }
        return new FormerResult("success", 0, "请求支付完成", null);
    }


    /**
     * 微信支付 统一下单 生成预支付交易单
     */
    public FormerResult appWXPayNotify(HttpServletRequest request,String orderNumber,double payMoney) throws Exception {

        //查询该交易订单
        OrderVo orderVo = orderService.findOrderByOrderNumber(orderNumber);
        //全部微信支付
        String totalFee = "";
        if(payMoney == 0.00){
            totalFee= String.valueOf(BigDecimalUtil.multiply(orderVo.getTotalPrice().doubleValue(),100));
            //部分微信支付
        }else {
            totalFee = String.valueOf(BigDecimalUtil.multiply(payMoney,100));
        }
        /**调用接口必须参数**/
        String url = PayConfig.UNIFIEDORDER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("appid",PayConfig.getApp_appID());
        params.put("mch_id",PayConfig.getApp_mchID());
        params.put("nonce_str", String.valueOf(new Date().getTime()));
        params.put("body", "懒人家洗衣-订单充值");
        params.put("out_trade_no", orderNumber);
        //充值金额
        params.put("total_fee", "1");
        InetAddress localAddr = InetAddress.getLocalHost();
        String localIp = localAddr.getHostAddress().toString();
        params.put("spbill_create_ip", localIp); //终端IP
        params.put("notify_url", "http://cwj1.ngrok2.xiaomiqiu.cn/WXPayNotifyUrl");  //异步通知回调地址
        params.put("trade_type", "APP"); //支付类型
        //将非空参数进行签名运算(排序，MD5加密)
        String sign = WXPayUtil.getSign(params,PayConfig.getApp_key());
        params.put("sign", sign);
        //将请求参数转化为微信支付要求的xml格式文件
        String xml = WXPayUtil.mapToXml1(params);

        //调用对方demo请求下单
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        String responseContent = "";
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(xml,"UTF-8"));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            System.out.println(responseContent);
            response.close();
            httpClient.close();

        } catch (ClientProtocolException e) {
            System.out.println(e);
            new FormerResult("success", 1, "网络请求异常，请稍后重试或联系客服！", null);
        } catch (IOException e) {
            System.out.println(e);
            new FormerResult("success", 1, "网络请求异常，请稍后重试或联系客服！", null);
        } finally {
            close(response, httpClient);
        }
        if (responseContent != null) {
            //将String转化成map
            Map resultMap = WXPayUtil.xmlToMap(responseContent);
            System.out.println("微信支付请求结果：" + resultMap);
            //请求返回结果的处理
            String returnCode = (String) resultMap.get("return_code");
            if (returnCode.equals("SUCCESS")) {
                String appid = (String) resultMap.get("appid");
                String mch_id = (String)resultMap.get("mch_id");
                String nonce_str = (String)resultMap.get("nonce_str");
                String resultSign = (String)resultMap.get("sign");
                String resultCode = (String) resultMap.get("result_code");
                if(resultCode.equals("SUCCESS")){
                    Map<String, String> payMap = new HashMap<String, String>();
                    String trade_type = (String)resultMap.get("trade_type");
                    String prepay_id = (String)resultMap.get("prepay_id");
                    payMap.put("appid", appid);
                    payMap.put("partnerid", mch_id);
                    payMap.put("prepayid", prepay_id);
                    payMap.put("noncestr", nonce_str);
                    payMap.put("timestamp", DateUtils.getCurrentDateToNum());
                    payMap.put("package", "Sign=WXPay");
                    String paySign = WXPayUtil.getSign((HashMap<String, String>) payMap,PayConfig.getApp_key());
                    ((HashMap<String, String>) payMap).put("sign", paySign);
                    return new FormerResult("success", 0, "预支付成功",payMap);
                }
            }else {
                new FormerResult("success", 1, (String) resultMap.get("return_msg"), null);
            }
        } else {
            new FormerResult("success", 1, "请求支付网关异常，请稍后重试或联系客服", null);
        }
        return null;
    }

    @RequestMapping(value = "/WXPayNotifyUrl",method = {RequestMethod.GET,RequestMethod.POST})
    public String MyWXPayNotifyUrl(HttpServletRequest request){
        Map<String,String> notifyMap = null;
        try {
            request.setCharacterEncoding("utf-8");
            String resString = WXPayUtil.parseRequst(request);

            notifyMap = WXPayUtil.xmlToMap(resString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //异步通知结果
        if(notifyMap.get("return_code").equals("SUCCESS")){
            //查询该交易订单
            OrderVo orderVo = orderService.findOrderByOrderNumber(notifyMap.get("out_trade_no"));
            //通知防重
            if(orderVo.getPayStatus() == 1){
                Map<String, String> syncMap = new HashMap<String, String>();
                syncMap.put("return_code","SUCCESS");
                syncMap.put("return_msg", "OK");
                String syncXml = WXPayUtil.mapToXml1(syncMap);
                return syncXml;
            }else {
                synchronized (lock) {
                    //验签
                    String resultSign = notifyMap.get("sign");
                    String mySign = "";
                    if(resultSign.equals(mySign)){
                        //取出所有参数
                        String appid = notifyMap.get("appid"); //微信分配的小程序ID
                        String mch_id = notifyMap.get("mch_id");
                        String nonce_str = notifyMap.get("nonce_str");
                        String result_code = notifyMap.get("result_code");
                        String openid = notifyMap.get("openid"); //用户在商户appid下的唯一标识
                        String is_subscribe = notifyMap.get("is_subscribe"); //用户是否关注公众账号，Y-关注，N-未关注
                        String trade_type = notifyMap.get("trade_type"); //交易类型
                        String bank_type = notifyMap.get("bank_type"); //付款银行
                        String total_fee = notifyMap.get("total_fee");
                        String cash_fee = notifyMap.get("cash_fee"); //现金支付金额
                        String transaction_id = notifyMap.get("transaction_id"); //微信支付订单号
                        String out_trade_no = notifyMap.get("out_trade_no");
                        String time_end = notifyMap.get("time_end");  //支付完成时间
                        //验金额
                        String totalFee = String.valueOf(BigDecimalUtil.multiply(orderVo.getTotalPrice().doubleValue(),100));
                        if(notifyMap.get("total_fee").equals(totalFee)){
                            //增加支付流水 记录
                            Map<String, Object> flowRecordMap = new HashMap<String, Object>();
                            flowRecordMap.put("trade_type", trade_type);
                            flowRecordMap.put("bank_type", bank_type);
                            flowRecordMap.put("total_fee", total_fee);
                            flowRecordMap.put("cash_fee", cash_fee);
                            flowRecordMap.put("transaction_id", transaction_id);
                            flowRecordMap.put("out_trade_no", out_trade_no);
                            flowRecordMap.put("time_end", time_end);
                            flowRecordMap.put("user_id", orderVo.getUserId());
                            flowRecordMap.put("trade_source", "微信APP交易");
                            payService.payFlowRecord(flowRecordMap);
                        }
                        //向上级返利
                         rebateService.rebate(orderVo);

                        //更改订单 支付状态
                        Integer status = 1;
                        orderService.updateOrderPayStatus(orderVo.getOrderNumber());
                        //根据订单号 加入懒人家助手 预约

                    }
                }
            }
        }
        return null;
    }

    /**
     *  支付宝 支付
     * @param request
     * @param orderNumber
     * @param payMoney
     * @return
     * @throws Exception
     */
    public FormerResult appAliPay(HttpServletRequest request,String orderNumber,double payMoney) throws Exception {
        //构建请求：公共参数
        String app_id = PayConfig.getApp_appID();
        String method = "alipay.trade.app.pay";
        String charset = "utf-8";
        String sign_type = "RSA2";
        String sign = "";
        String timestamp = DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
        String version = "1.0";
        String notify_url = "http://cwj1.ngrok2.xiaomiqiu.cn/AliPayNotifyUrl";
        //业务参数封装
        String biz_content = "";
        //构建请求：业务参数
        JSONObject bizContentJSON=new JSONObject();
        bizContentJSON.put("subject", "懒人家订单");
        bizContentJSON.put("out_trade_no", orderNumber);
        bizContentJSON.put("total_amount", "0.01");//String.valueOf(BigDecimalUtil.round(payMoney, 2, BigDecimalUtil.DEF_SCALE_4)));
        bizContentJSON.put("product_code", "QUICK_MSECURITY_PAY");
        bizContentJSON.put("timeout_express","90m");

        biz_content = String.valueOf(bizContentJSON);
      //支付宝支付方式一：使用SDK支付
        AlipayClient alipayClient = new DefaultAlipayClient(
                PayConfig.getAlipayUrl(),
                PayConfig.getAlipayAppId(),
                PayConfig.getAlipayAppPrivateKey(),
                PayConfig.getAlipayFormat(),
                PayConfig.getAlipayCharset(),
                PayConfig.getAlipayPublicKey(),
                PayConfig.getAlipaySignType());
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest payRequest = new AlipayTradeAppPayRequest();
        payRequest.setBizContent(biz_content);
        payRequest.setNotifyUrl("http://cwj1.ngrok2.xiaomiqiu.cn/AliPayNotifyUrl");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(payRequest);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            if(response.isSuccess()){
                System.out.println("调用成功");
                return new FormerResult("SUCCESS", 0, "支付订单构建成功", response.getBody());
            } else {
                System.out.println("调用失败");
                return new FormerResult("SUCCESS", 1, "支付订单构建失败",null);
            }
        } catch (AlipayApiException e) {
            return new FormerResult("SUCCESS",1,"支付失败",e);
        }
       //支付宝支付方式二：自定义参数和流程支付
    }

    @RequestMapping(value = "/AliPayNotifyUrl",method = {RequestMethod.GET,RequestMethod.POST})
    public String AliPayNotifyUrl(HttpServletRequest request){
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, PayConfig.getAlipayPublicKey(), PayConfig.getAlipayCharset(),"RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
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
