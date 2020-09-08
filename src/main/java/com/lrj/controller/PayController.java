package com.lrj.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.lrj.VO.*;
import com.lrj.config.PayConfig;
import com.lrj.mapper.ReservationMapper;
import com.lrj.pojo.BalanceRecord;
import com.lrj.pojo.PayOperation;
import com.lrj.service.*;
import com.lrj.pojo.Balance;
import com.lrj.service.IBalanceService;
import com.lrj.service.IOrderService;
import com.lrj.service.IPayService;

import com.lrj.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private IBalanceService balanceService;
    @Resource
    private RebateService rebateService;
    @Resource
    private ReservationMapper reservationMapper;
    @Resource
    private IUserService userService;

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
        String orderNumber = "";
        //增加余额充值记录
        BalanceRecord balanceRecord = new BalanceRecord();
        balanceRecord.setCreateTime(DateUtils.getNowDateTime());
        balanceRecord.setType(1);
        balanceRecord.setUserId(userId);
        balanceRecord.setStatus(0);
        switch (rechargeType){
            //充值100 送20%余额
            case 1:
                // 生成订单号
                orderNumber = DateUtils.getNewdateBygs("yyyyMMdd") + RandomUtil.generateOrder(6)+"A";
                balanceRecord.setRechargeOrderNumber(orderNumber);
                balanceRecord.setAmount(new BigDecimal(100.00));
                payService.addUserBalanceRecord(balanceRecord);
                if(payType == 1){
                    FormerResult formerResult = appWXPay(request, orderNumber, 100.00);
                    return formerResult;
                    //支付宝支付
                }else if(payType == 2){
                    FormerResult formerResult = appAliPay(request, orderNumber, 100.00);
                    return formerResult;
                }
                break;
            //充值300 送25%余额
            case 2:
                // 生成订单号
                orderNumber = DateUtils.getNewdateBygs("yyyyMMdd") + RandomUtil.generateOrder(6)+"B";
                balanceRecord.setRechargeOrderNumber(orderNumber);
                balanceRecord.setAmount(new BigDecimal(300.00));
                payService.addUserBalanceRecord(balanceRecord);
                if(payType == 1){
                    FormerResult formerResult = appWXPay(request, orderNumber, 300.00);
                    return formerResult;
                    //支付宝支付
                }else if(payType == 2){
                    FormerResult formerResult = appAliPay(request, orderNumber, 300.00);
                    return formerResult;
                }
                break;
            //充值500 送30%余额
            case 3:
                // 生成订单号
                orderNumber = DateUtils.getNewdateBygs("yyyyMMdd") + RandomUtil.generateOrder(6)+"C";
                balanceRecord.setRechargeOrderNumber(orderNumber);
                balanceRecord.setAmount(new BigDecimal(500.00));
                payService.addUserBalanceRecord(balanceRecord);
                if(payType == 1){
                    FormerResult formerResult = appWXPay(request, orderNumber, 500.00);
                    return formerResult;
                    //支付宝支付
                }else if(payType == 2){
                    FormerResult formerResult = appAliPay(request, orderNumber, 500.00);
                    return formerResult;
                }
                break;
            //充值1000 送40%余额
            case 4:
                // 生成订单号
                orderNumber = DateUtils.getNewdateBygs("yyyyMMdd") + RandomUtil.generateOrder(6)+"D";
                balanceRecord.setRechargeOrderNumber(orderNumber);
                balanceRecord.setAmount(new BigDecimal(1000.00));
                payService.addUserBalanceRecord(balanceRecord);
                if(payType == 1){
                    FormerResult formerResult = appWXPay(request, orderNumber, 1000.00);
                    return formerResult;
                    //支付宝支付
                }else if(payType == 2){
                    FormerResult formerResult = appAliPay(request, orderNumber, 1000.00);
                    return formerResult;
                }
                break;
            //自定义充值   不送
            case 5:
                // 生成订单号
                orderNumber = DateUtils.getNewdateBygs("yyyyMMdd") + RandomUtil.generateOrder(6)+"E";
                balanceRecord.setRechargeOrderNumber(orderNumber);
                BigDecimal rechargeMoney = new BigDecimal(request.getParameter("rechargeMoney"));
                balanceRecord.setAmount(rechargeMoney);
                payService.addUserBalanceRecord(balanceRecord);
                if(payType == 1){
                    FormerResult formerResult = appWXPay(request, orderNumber, rechargeMoney.doubleValue());
                    return formerResult;
                    //支付宝支付
                }else if(payType == 2){
                    FormerResult formerResult = appAliPay(request, orderNumber, rechargeMoney.doubleValue());
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
                FormerResult formerResult = appWXPay(request, orderNumber, 0.00);
                return formerResult;
            //支付宝支付
            }else if(payType == 2){
                FormerResult formerResult = appAliPay(request, orderNumber, 0.00);
                return formerResult;
            }
            return new FormerResult("success", 0, "请求支付完成", null);
            //使用
        } else if (isBalance == 1) {
            Balance userBalance = userService.getUserBalanceInfo(userId);
            //如果余额金额大于等于支付金额
            if (userBalance.getBalance().doubleValue() >= orderVo.getTotalPrice().doubleValue()) {
                double balanceMoney = userBalance.getBalance().doubleValue() - orderVo.getTotalPrice().doubleValue();
                //修改余额
                balanceService.updateUserBalance(balanceMoney,userId);
                //增加余额变动记录
                BalanceRecord balanceRecord = new BalanceRecord();
                balanceRecord.setAmount(orderVo.getTotalPrice());
                balanceRecord.setCreateTime(DateUtils.getNowDateTime());
                balanceRecord.setType(2);
                balanceRecord.setUserId(orderVo.getUserId());
                balanceRecord.setStatus(1);
                payService.addUserBalanceRecord(balanceRecord);
                //向上级返利
                rebateService.rebate(orderVo);
                //更改订单 支付状态
                orderService.updateOrderPayStatus(orderVo.getOrderNumber());
                //如果是单项洗衣或单项家政（更改预约单 状态）
                if (orderVo.getOrderType() == 1 || orderVo.getOrderType() == 3) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("orderNumber", orderVo.getOrderNumber());
                    params.put("status", 3);
                    Integer updateNum = reservationMapper.updateReservationStatus(params);
                }
                new FormerResult("success", 1, "请求支付完成", null);
            } else {
                double payMoney = orderVo.getTotalPrice().doubleValue() - userBalance.getBalance().doubleValue();
                //修改余额(余额清零)
                balanceService.updateUserBalance(0.00,userId);
                //增加余额变动记录
                BalanceRecord balanceRecord = new BalanceRecord();
                balanceRecord.setAmount(userBalance.getBalance());
                balanceRecord.setCreateTime(DateUtils.getNowDateTime());
                balanceRecord.setType(2);
                balanceRecord.setUserId(orderVo.getUserId());
                payService.addUserBalanceRecord(balanceRecord);
                if(payType == 1){   //微信支付
                    appWXPay(request, orderNumber, payMoney);
                }else if(payType == 2){   //支付宝支付
                    appAliPay(request, orderNumber, payMoney);
                }else {
                    return new FormerResult("SUCCESS", 1, "请求支付异常", null);
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
    public FormerResult appWXPay(HttpServletRequest request,String orderNumber,double payMoney) throws Exception {

        //查询该交易订单
        OrderVo orderVo = orderService.findOrderByOrderNumber(orderNumber);
        //全部微信支付
        String totalFee = "";
        if(payMoney == 0.00){
            totalFee = new BigDecimal(orderVo.getTotalPrice().doubleValue()*100).setScale(0).toString();//保留0位小数
            //部分微信支付
        }else {
            totalFee = new BigDecimal(payMoney*100).setScale(0).toString();
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
        params.put("total_fee", totalFee);
        InetAddress localAddr = InetAddress.getLocalHost();
        String localIp = localAddr.getHostAddress().toString();
        params.put("spbill_create_ip", localIp); //终端IP
        params.put("notify_url", "http://www.51lrj.com/WXPayNotifyUrl");  //异步通知回调地址
        params.put("trade_type", "APP"); //支付类型
        //将非空参数进行签名运算(排序，MD5加密)
        /*Map<String, String> params2 = null;
        for (String key: params.keySet()) {
            String value = (String) params.get(key);
            if (value == null) {
                value = "";
            }
            params2.put(key, value);
        }*/
        String sign = WXPayUtil.getSign(params,PayConfig.getApp_key());
        params.put("sign", sign);
        System.out.println("我支付发送的签名："+sign);
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
                    System.out.println("生成预付单以后的签名："+paySign);
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
    public String MyWXPayNotifyUrl(HttpServletRequest request) throws Exception {
        Map<String, String> notifyMap = null;
        try {
            request.setCharacterEncoding("utf-8");
            String resString = WXPayUtil.parseRequst(request);
            System.out.println("微信返回通知的xml:"+resString);
            notifyMap = WXPayUtil.xmlToMap(resString);
            System.out.println("微信返回通知的Map："+notifyMap);
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //异步通知结果
        if (notifyMap.get("return_code").equals("SUCCESS")) {
            //判断是充值订单
            if (notifyMap.get("out_trade_no").contains("A") || notifyMap.get("out_trade_no").contains("B") || notifyMap.get("out_trade_no").contains("C") || notifyMap.get("out_trade_no").contains("D") || notifyMap.get("out_trade_no").contains("E")) {
                //通知防重
                //查询该笔充值交易 是否已完成
                BalanceRecord balanceRecord = balanceService.findBalanceByRechargeOrderNumber(notifyMap.get("out_trade_no"));
                if (balanceRecord.getStatus() == 1) {
                    String syncXml = "<xml> \n" +
                            "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                            "</xml>";
                    return syncXml;
                } else if (balanceRecord.getStatus() == 0) {
                    synchronized (lock) {
                        //验签
                        if (WXPayUtil.isSignatureValid(notifyMap, PayConfig.getApp_key())) {
                            //增加用户资金流水(不赠送+赠送)
                            String out_trade_no = notifyMap.get("out_trade_no");
                            String total_fee = notifyMap.get("total_fee");
                            if (out_trade_no.contains("E")) {
                                //完成充值单
                                payService.updateUserBalanceRecord(balanceRecord.getRechargeOrderNumber());
                                //修改用户余额(只有充值金额)
                                Balance userBalance = userService.getUserBalanceInfo(balanceRecord.getUserId());
                                balanceService.updateUserBalance(userBalance.getBalance().doubleValue()+balanceRecord.getAmount().doubleValue(),balanceRecord.getUserId());
                            } else if (out_trade_no.contains("A") || out_trade_no.contains("B") || out_trade_no.contains("C") || out_trade_no.contains("D")) {
                                payService.updateUserBalanceRecord(balanceRecord.getRechargeOrderNumber());
                                //完成 赠送余额 记录
                                BalanceRecord balanceRecord2 = new BalanceRecord();
                                balanceRecord2.setCreateTime(DateUtils.getNowDateTime());
                                balanceRecord2.setUserId(balanceRecord.getUserId());
                                BigDecimal amount = null;
                                //不同充值，不同赠送
                                if (out_trade_no.contains("A")) {
                                    amount = new BigDecimal((new BigDecimal(total_fee).doubleValue()/100) * 0.2).setScale(2, BigDecimal.ROUND_HALF_UP);
                                } else if (out_trade_no.contains("B")) {
                                    amount = new BigDecimal((new BigDecimal(total_fee).doubleValue()/100) * 0.25).setScale(2, BigDecimal.ROUND_HALF_UP);
                                } else if (out_trade_no.contains("C")) {
                                    amount = new BigDecimal((new BigDecimal(total_fee).doubleValue()/100) * 0.3).setScale(2, BigDecimal.ROUND_HALF_UP);
                                } else if (out_trade_no.contains("D")) {
                                    amount = new BigDecimal((new BigDecimal(total_fee).doubleValue()/100) * 0.4).setScale(2, BigDecimal.ROUND_HALF_UP);
                                }
                                balanceRecord2.setAmount(amount);
                                balanceRecord2.setType(4);
                                balanceRecord2.setStatus(1);
                                payService.addUserBalanceRecord(balanceRecord2);
                                //增加支付流水 记录
                                PayOperation payOperation = new PayOperation();
                                payOperation.setTradeSource(2);
                                payOperation.setTradeType(1);
                                payOperation.setBankType(notifyMap.get("bank_type"));
                                payOperation.setTotalFee(new BigDecimal(total_fee).setScale(2, BigDecimal.ROUND_HALF_UP));
                                payOperation.setTransactionId(notifyMap.get("transaction_id"));
                                payOperation.setOutTradeNo(out_trade_no);
                                payOperation.setCreateTime(DateUtils.getNowTime("yyyy-MM-DD HH:mm:ss"));
                                payOperation.setUserId(balanceRecord.getUserId());
                                payOperation.setUserPhone("");
                                payService.payFlowRecord(payOperation);
                                //修改用户余额(充值金额+赠送金额)
                                Balance userBalance = userService.getUserBalanceInfo(balanceRecord.getUserId());
                                balanceService.updateUserBalance(userBalance.getBalance().doubleValue()+balanceRecord.getAmount().doubleValue()+balanceRecord2.getAmount().doubleValue(),balanceRecord.getUserId());
                            }
                        }
                    }
                }
             //判断是正常下单订单
            } else {
                //查询该交易订单
                OrderVo orderVo = orderService.findOrderByOrderNumber(notifyMap.get("out_trade_no"));
                //通知防重
                if (orderVo.getPayStatus() == 1) {
                    Map<String, String> syncMap = new HashMap<String, String>();
                    syncMap.put("return_code", "SUCCESS");
                    String syncXml = WXPayUtil.mapToXml1(syncMap);
                    return syncXml;
                } else {
                    synchronized (lock) {
                        //验签
                        if (WXPayUtil.isSignatureValid(notifyMap, PayConfig.getApp_key())) {
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
                            //增加支付流水 记录
                            PayOperation payOperation = new PayOperation();
                            payOperation.setTradeSource(2);
                            payOperation.setTradeType(1);
                            payOperation.setBankType(bank_type);
                            payOperation.setTotalFee(new BigDecimal(total_fee).setScale(2, BigDecimal.ROUND_HALF_UP));
                            payOperation.setTransactionId(transaction_id);
                            payOperation.setOutTradeNo(out_trade_no);
                            payOperation.setCreateTime(DateUtils.getNowTime("yyyy-MM-DD HH:mm:ss"));
                            payOperation.setUserId(orderVo.getUserId());
                            payOperation.setUserPhone(orderVo.getUserPhone());
                            payService.payFlowRecord(payOperation);
                            //向上级返利
                            rebateService.rebate(orderVo);
                            //更改订单 支付状态
                            orderService.updateOrderPayStatus(orderVo.getOrderNumber());
                            //如果是单项洗衣或单项家政（更改预约单 状态）
                            if (orderVo.getOrderType() == 1 || orderVo.getOrderType() == 3) {
                                Map<String, Object> params = new HashMap<>();
                                params.put("orderNumber", orderVo.getOrderNumber());
                                params.put("status", 3);
                                Integer updateNum = reservationMapper.updateReservationStatus(params);
                            }
                        }
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
        String notify_url = "http://goodbadluck.myds.me:8083/AliPayNotifyUrl";
        //业务参数封装
        String biz_content = "";
        //构建请求：业务参数
        JSONObject bizContentJSON=new JSONObject();
        bizContentJSON.put("subject", "懒人家订单");
        bizContentJSON.put("out_trade_no", orderNumber);
        //查询该交易订单
        OrderVo orderVo = orderService.findOrderByOrderNumber(orderNumber);
        //全部支付宝支付
        String totalFee = "";
        if(payMoney == 0.00){
            totalFee = orderVo.getTotalPrice().toString();
            //部分支付宝支付
        }else {
            totalFee = new BigDecimal(payMoney).toString();
        }
        bizContentJSON.put("total_amount",totalFee);//
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
        payRequest.setNotifyUrl("http://www.51lrj.com/AliPayNotifyUrl");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(payRequest);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            if(response.isSuccess()){
                System.out.println("支付宝支付调用成功");
                return new FormerResult("SUCCESS", 0, "支付订单构建成功", response.getBody());
            } else {
                System.out.println("支付宝支付调用失败");
                return new FormerResult("SUCCESS", 1, "支付订单构建失败",null);
            }
        } catch (AlipayApiException e) {
            return new FormerResult("SUCCESS",1,"支付失败",e);
        }
       //支付宝支付方式二：自定义参数和流程支付
    }

    @RequestMapping(value = "/AliPayNotifyUrl",method = {RequestMethod.GET,RequestMethod.POST})
    public String AliPayNotifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        System.out.println("支付宝返回参数："+params);

        //异步通知结果
        if (params.get("trade_status").equals("TRADE_SUCCESS")) {
            //判断是充值订单
            if (params.get("out_trade_no").contains("A") || params.get("out_trade_no").contains("B") || params.get("out_trade_no").contains("C") || params.get("out_trade_no").contains("D") || params.get("out_trade_no").contains("E")) {
                //通知防重
                //查询该笔充值交易 是否已完成
                BalanceRecord balanceRecord = balanceService.findBalanceByRechargeOrderNumber(params.get("out_trade_no"));
                if (balanceRecord.getStatus() == 1) {
                     response.getWriter().write("success");
                } else if (balanceRecord.getStatus() == 0) {
                    synchronized (lock) {
                        //验签
                        //在此代码行。。。。验证不通过。。需咨询支付宝公司
                        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
                        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
                            //增加用户资金流水(不赠送+赠送)
                            String out_trade_no = params.get("out_trade_no");
                            String total_amount = params.get("total_amount");
                            if (out_trade_no.contains("E")) {
                                //完成充值单
                                payService.updateUserBalanceRecord(balanceRecord.getRechargeOrderNumber());
                                //修改用户余额(只有充值金额)
                                Balance userBalance = userService.getUserBalanceInfo(balanceRecord.getUserId());
                                balanceService.updateUserBalance(userBalance.getBalance().doubleValue()+balanceRecord.getAmount().doubleValue(),balanceRecord.getUserId());
                            } else if (out_trade_no.contains("A") || out_trade_no.contains("B") || out_trade_no.contains("C") || out_trade_no.contains("D")) {
                                payService.updateUserBalanceRecord(balanceRecord.getRechargeOrderNumber());
                                //完成 赠送余额 记录
                                BalanceRecord balanceRecord2 = new BalanceRecord();
                                balanceRecord2.setCreateTime(DateUtils.getNowDateTime());
                                balanceRecord2.setUserId(balanceRecord.getUserId());
                                BigDecimal amount = null;
                                //不同充值，不同赠送
                                if (out_trade_no.contains("A")) {
                                    amount = new BigDecimal(new BigDecimal(total_amount).doubleValue() * 0.2).setScale(2, BigDecimal.ROUND_HALF_UP);
                                } else if (out_trade_no.contains("B")) {
                                    amount = new BigDecimal(new BigDecimal(total_amount).doubleValue() * 0.25).setScale(2, BigDecimal.ROUND_HALF_UP);
                                } else if (out_trade_no.contains("C")) {
                                    amount = new BigDecimal(new BigDecimal(total_amount).doubleValue() * 0.3).setScale(2, BigDecimal.ROUND_HALF_UP);
                                } else if (out_trade_no.contains("D")) {
                                    amount = new BigDecimal(new BigDecimal(total_amount).doubleValue() * 0.4).setScale(2, BigDecimal.ROUND_HALF_UP);
                                }
                                balanceRecord2.setAmount(amount);
                                balanceRecord2.setType(4);
                                balanceRecord2.setStatus(1);
                                payService.addUserBalanceRecord(balanceRecord2);
                                //增加支付流水 记录
                                PayOperation payOperation = new PayOperation();
                                payOperation.setTradeSource(3);
                                payOperation.setTradeType(1);
                                payOperation.setTotalFee(new BigDecimal(params.get("total_amount")));
                                payOperation.setTransactionId(params.get("trade_no"));
                                payOperation.setOutTradeNo(params.get("out_trade_no"));
                                payOperation.setCreateTime(DateUtils.getNowTime("yyyy-MM-DD HH:mm:ss"));
                                payOperation.setUserId(balanceRecord.getUserId());
                                payOperation.setUserPhone("");
                                payService.payFlowRecord(payOperation);
                                //修改用户余额(充值金额+赠送金额)
                                Balance userBalance = userService.getUserBalanceInfo(balanceRecord.getUserId());
                                balanceService.updateUserBalance(userBalance.getBalance().doubleValue()+balanceRecord.getAmount().doubleValue()+balanceRecord2.getAmount().doubleValue(),balanceRecord.getUserId());
                            }
                    }
                }
             //判断是正常下单订单
            }else{
                //查询该交易订单
                OrderVo orderVo = orderService.findOrderByOrderNumber(params.get("out_trade_no"));
                //增加支付流水 记录
                PayOperation payOperation = new PayOperation();
                payOperation.setTradeSource(3);
                payOperation.setTradeType(1);
                payOperation.setTotalFee(new BigDecimal(params.get("total_amount")));
                payOperation.setTransactionId(params.get("trade_no"));
                payOperation.setOutTradeNo(params.get("out_trade_no"));
                payOperation.setCreateTime(DateUtils.getNowTime("yyyy-MM-DD HH:mm:ss"));
                payOperation.setUserId(orderVo.getUserId());
                payOperation.setUserPhone(orderVo.getUserPhone());
                payService.payFlowRecord(payOperation);
                //向上级返利
                rebateService.rebate(orderVo);
                //更改订单 支付状态
                orderService.updateOrderPayStatus(orderVo.getOrderNumber());
                //如果是单项洗衣或单项家政（更改预约单 状态）
                if (orderVo.getOrderType() == 1 || orderVo.getOrderType() == 3) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("orderNumber", orderVo.getOrderNumber());
                    param.put("status", 3);
                    Integer updateNum = reservationMapper.updateReservationStatus(param);
                }
            }
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
