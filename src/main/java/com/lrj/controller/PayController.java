package com.lrj.controller;

import com.lrj.VO.*;
import com.lrj.config.PayConfig;
import com.lrj.pojo.TeamLaundry;
import com.lrj.service.*;

import com.lrj.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.OBJ_ADAPTER;
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
    @Resource
    private IUserService userService;
    @Resource
    private TeamLaundryService teamLaundryService;
    @Resource
    private IBalanceService iBalanceService;
    @Resource
    private IMemberServiceUserRelationService iMemberServiceUserRelationService;

    /*
    * 获取用户支付信息
    * */
    @RequestMapping("/getUserPayInfo")
    public FormerResult getUserPayInfo(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        FormerResult result = new FormerResult();
        try {
           /* *//** 校验必须字段 母账号账号用户id**//*
            if (RequestParameterUtil.checkRequestParametersIsNull(request, new String[]{"userId"}, result)) {
                return result;
            }
            Integer userId = (Integer.parseInt(request.getParameter("userId")));*/
            Integer userId = 7;
            //获得app用户可用额度
            TeamLaundry teamLaundry = teamLaundryService.findTeamLundryByUserId(userId);
            if (teamLaundry==null) {
                map.put("isPass", 2);
                map.put("remainLimit", 0);
            } else {
                map.put("isPass", 1);
                map.put("remainLimit", teamLaundry.getCountLimit().subtract(teamLaundry.getUseLimit()));
            }
            UserMoneyInfo userMoneyInfo = iBalanceService.findBalanceByUserId(userId, result, request);
            map.put("userMoneyInfo",userMoneyInfo);
            //获取用户当月剩余额度用于支付
            /** 查询用户当月额度 **/
            BigDecimal residueLimit = iMemberServiceUserRelationService.findResidueLimitByUserId(userId);
            /** 判断是否已经是会员 **/
            if (!iMemberServiceUserRelationService.judgeTheUserIsMemberService(userId)) {
                /** 额度0 **/
                map.put("residueLimit", 0);
            }else {
                map.put("residueLimit",residueLimit);
            }
            //判断用户是否是会员接口
            /** 调用判断会员业务方法 **/
            Integer isMemberService = iMemberServiceUserRelationService.judgeTheUserIsMemberService(userId)==true ? 1 : 0;
            map.put("isMemberService",isMemberService);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return CommonUtil.FAIL(result,"",e.getMessage());
        }
        return CommonUtil.SUCCESS(result,"查询成功!",map);
    }
    /**
     * 微信支付 统一下单 生成预支付交易单
     */
    @RequestMapping(value = "/weixinPay", method = {RequestMethod.GET, RequestMethod.POST})
    public FormerResult appWXPay(HttpServletRequest request) throws Exception {

        /**接收前端传回参数**/
        String orderNumber = request.getParameter("orderNumber");
        Integer userId = Integer.parseInt(request.getParameter("userId"));

        /** 校验必须参数 **/
        if (orderNumber == null || userId ==0) {
            return new FormerResult("success", 1, "参数有误,请检查参数", null);
        }
        //查询该交易订单
        OrderVo orderVo = orderService.findOrderByOrderNumber(orderNumber);
        /**调用接口必须参数**/
        String url = PayConfig.UNIFIEDORDER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("appid",PayConfig.getApp_appID());
        params.put("mch_id",PayConfig.getApp_mchID());
        params.put("nonce_str", String.valueOf(new Date().getTime()));
        params.put("body", "懒人家洗衣-订单充值");
        params.put("out_trade_no", orderNumber);
        //计算充值金额
        int amount =0;
        try {
            BigDecimal bg = new BigDecimal(String.valueOf(orderVo.getTotalPrice()));
            amount = bg.setScale(2, BigDecimal.ROUND_HALF_UP).intValue();

        } catch (Exception e) {
            new FormerResult("success", 1, "充值金额的格式不正确", null);
        }
        params.put("total_fee", "2400"); //String.valueOf(BigDecimalUtil.multiply(amount,100))
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
            Map resultMap = WXPayUtil.xmlToMap(String.valueOf(responseContent));
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
        //异步通知结果
        Map<String,String> notifyMap = null;
        if(notifyMap.get("return_code").equals("SUCCESS")){
            //查询该交易订单
            OrderVo orderVo = orderService.findOrderByOrderNumber(notifyMap.get("out_trade_no"));
            //通知防重
            if(orderVo.getPayStatus() == 0){
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
                            payService.WXPayFlowRecord(flowRecordMap);
                        }
                        //向上级返利
                        UserInfoVo userInfoVo = userService.findUserInfoByUserId(orderVo.getUserId());
                        UserLevelVo userLevelVo = userService.findUserLevelInfo(orderVo.getUserId());
                        double feeBackMoney = userLevelVo.getDistributionRatio().doubleValue()*Integer.parseInt(total_fee);
                        UserInfoVo userInfoVoFather = userService.findUserByInviteCode(userInfoVo.getInvitedCode());
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("userId", userInfoVoFather.getAppUserId());
                        params.put("invitedId", userInfoVo.getAppUserId());
                        params.put("backMoney", feeBackMoney);
                        params.put("createTime", DateUtils.getNowDateTime());
                        params.put("status", "1");
                        Integer insertNum = userService.giveFeeBack(params);
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
