package com.lrj.service.impl;

import com.lrj.VO.FormerResult;
import com.lrj.common.Constant;
import com.lrj.config.PayConfig;
import com.lrj.mapper.BalanceMapper;
import com.lrj.mapper.IOrderMapper;
import com.lrj.mapper.PayOperationMapper;
import com.lrj.mapper.ThirdAccMapper;
import com.lrj.pojo.Balance;
import com.lrj.pojo.Order;
import com.lrj.pojo.PayOperation;
import com.lrj.pojo.ThirdAcc;
import com.lrj.service.AppletsPayService;
import com.lrj.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lrj.pojo.Order.ORDER_NUMBER;
import static com.lrj.pojo.ThirdAcc.COLUMN_THIRDACC_PHONE;

/**
 * @Description:
 * @Author: Lxh
 * @Date: 2020/6/23 16:03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppletsPayServiceImpl implements AppletsPayService {

    @Resource
    private IOrderMapper orderMapper;

    @Resource
    private BalanceMapper balanceMapper;

    @Resource
    private PayOperationMapper payOperationMapper;

    @Resource
    private ThirdAccMapper thirdAccMapper;

    private static final Logger log = LoggerFactory.getLogger(AppletsPayServiceImpl.class);

    /**
     * @param: request
     * @Description: 微信第三方支付
     * @Author: LxH
     * @Date: 2020/6/23 16:05
     */
    @Override
    public FormerResult pay(HttpServletRequest request) {
        double userBalance = 0.00;
        /**接收前端传回参数*/
        String orderNumber = request.getParameter("orderNumber");
        Integer isBalance = Integer.parseInt(request.getParameter("isBalance"));
        /**校验必须参数**/
        if (orderNumber == null || isBalance == null) {
            return new FormerResult("success", 1, "参数有误,请检查参数", null);
        }
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo(ORDER_NUMBER,orderNumber);
        List<Order> orders = orderMapper.selectByExample(example);
        if (orders.size()==0) {
            return CommonUtil.FAIL(new FormerResult(),null,null);
        }
        for (Order order : orders) {
            //不使用余额支付
            if (isBalance == 0) {
                return appletsPay(request,userBalance);
            } else if (isBalance == 1) {
                Balance balance = balanceMapper.selectByPrimaryKey(order.getUserId());
                //使用余额支付
                if (balance.getBalance().doubleValue() >= order.getTotalPrice().doubleValue()) {
                    userBalance =  balance.getBalance().doubleValue() - order.getTotalPrice().doubleValue();
                    balance.setBalance(new BigDecimal(userBalance));
                    //更新用户余额
                    balanceMapper.updateByPrimaryKeySelective(balance);
                    //添加流水记录
                    PayOperation payOperation = new PayOperation();
                    payOperation.setUserId(order.getUserId()).setTotalFee(order.getTotalPrice()).setOutTradeNo(orderNumber).
                            setUserPhone(order.getUserPhone()).setTradeSource(1).setTradeType(1).setTransactionId("余额支付").
                            setCreateTime(DateUtils.formatDate(new Date()));
                    payOperationMapper.insertSelective(payOperation);
                    order.setStatus(3).setPayStatus(1);
                    orderMapper.updateByPrimaryKeySelective(order);
                    return new FormerResult(Constant.SUCCESS,Constant.YES,"余额支付成功",null);
                } else {
                    //用户余额小于订单总价
                    userBalance =  order.getTotalPrice().doubleValue() - balance.getBalance().doubleValue();
                    //更新用户余额
                    balance.setBalance(new BigDecimal("0.00"));
                    balanceMapper.updateByPrimaryKeySelective(balance);
                    //剩余价格微信支付
                    return appletsPay(request, userBalance);
                }
            }
        }
        return null;
    }

    /**
     * @param: request
     * @param: response
     * @Description: 微信第三方支付回调
     * @Author: LxH
     * @Date: 2020/6/24 15:55
     */
    @Override
    public void appletsPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Order reOrder = new Order();
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        System.out.println("接收到的报文：" + notityXml);
        Map map = WXPayUtil.xmlToMap(notityXml);

        String returnCode = (String) map.get("return_code");
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            Map<String, String> validParams = WXPayUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String validStr = WXPayUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String sign = WXPayUtil.sign(validStr, PayConfig.getApp_key(), "utf-8").toUpperCase();//拼装生成服务器端验证的签名
            // 因为微信回调会有八次之多,所以当第一次回调成功了,那么我们就不再执行逻辑了

            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if(sign.equals(map.get("sign"))){
                //添加流水记录
                Example example = new Example(Order.class);
                example.createCriteria().andEqualTo(ORDER_NUMBER,map.get("out_trade_no"));
                List<Order> orders = orderMapper.selectByExample(example);
                for (Order order : orders) {
                    reOrder = order;
                    order.setStatus(3).setPayStatus(1);
                    orderMapper.updateByPrimaryKeySelective(order);
                }
                PayOperation payOperation = new PayOperation();
                payOperation.setTradeSource(2).setOutTradeNo(reOrder.getOrderNumber()).setUserPhone(reOrder.getUserPhone()).
                        setTransactionId(String.valueOf(map.get("transaction_id"))).setTotalFee(new BigDecimal(String.valueOf(map.get("total_fee")))).
                        setTradeType(1).setBankType(String.valueOf(map.get("bank_type")));
                int i = payOperationMapper.insertSelective(payOperation);
                System.out.println("添加成功"+1);
                //通知微信服务器已经支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            } else {
                System.out.println("微信支付回调失败!签名不一致");
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println(resXml);
        System.out.println("微信支付回调数据结束");

        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * @Description: 微信预支付
     * @Author: LxH
     * @Date: 2020/6/24 17:12
     */
    private FormerResult appletsPay(HttpServletRequest request, double userBalance) {
        Order reOrder = new Order();
        String value;
        String openId = "";
        String orderNumber = request.getParameter("orderNumber");
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo(ORDER_NUMBER,orderNumber);
        List<Order> orders = orderMapper.selectByExample(example);
        for (Order order : orders) {
            reOrder = order;
        }
        Example e = new Example(ThirdAcc.class);
        e.createCriteria().andEqualTo(COLUMN_THIRDACC_PHONE,reOrder.getUserPhone());
        List<ThirdAcc> thirdAccs = thirdAccMapper.selectByExample(e);
        for (ThirdAcc thirdAcc : thirdAccs) {
            openId = thirdAcc.getOpenId();
        }
        //全部微信支付
        if (userBalance==0.00) {
            value = String.valueOf(new BigDecimal(String.valueOf(reOrder.getTotalPrice())).movePointRight(2).intValue());
        } else {
            //部分微信支付
            value = String.valueOf(new BigDecimal(String.valueOf(userBalance)).movePointRight(2).intValue());
        }
        //调用接口必须参数
        String url = PayConfig.UNIFIEDORDER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("appid",PayConfig.getApp_appID());
        params.put("mch_id",PayConfig.getApp_mchID());
        params.put("nonce_str", StringUtils.getRandomStringByLength(32));
        params.put("body", "懒人家-小程序订单");
        params.put("out_trade_no", orderNumber);
        params.put("total_fee", value);
        params.put("spbill_create_ip", IpUtils.getIpAddr(request)); //终端IP
        params.put("notify_url", PayConfig.notify_url);  //异步通知回调地址
        params.put("trade_type", PayConfig.TRADETYPE); //支付类型
        params.put("openid",openId);
        //将非空参数进行签名运算(排序，MD5加密)

        String sign = WXPayUtil.getSign(params,PayConfig.getApp_key());
        System.out.println("第一次签名"+sign);
        log.info(sign);
        params.put("sign", sign);
        //将请求参数转化为微信支付要求的xml格式文件
        String xml = WXPayUtil.mapToXml1(params);
        String result = HttpClientTool.doPostByXml(url, xml);
        //将String转化成map
        try {
            Map resultMap = WXPayUtil.xmlToMap(result);
            System.out.println(resultMap);
            //请求返回结果的处理
            String returnCode = (String) resultMap.get("return_code");
            String resultCode = (String)resultMap.get("result_code");
            if ("SUCCESS".equals(returnCode) && returnCode.equals(resultCode)){
                HashMap<String, String> map = new HashMap<>();
                map.put("appId", PayConfig.getApp_appID());
                map.put("timeStamp", String.valueOf(System.currentTimeMillis()));
                //这边的随机字符串必须是第一次生成sign时，微信返回的随机字符串，不然小程序支付时会报签名错误
                map.put("nonceStr", String.valueOf(resultMap.get("nonce_str")));
                map.put("package", "prepay_id=" + resultMap.get("prepay_id"));
                map.put("signType", PayConfig.SIGNTYPE);
                String paySign = WXPayUtil.getSign(map, PayConfig.getApp_key());
                System.out.println("第二次签名"+paySign);
                log.info(paySign);
                map.put("paySign",paySign);
                return new FormerResult(Constant.SUCCESS,Constant.YES,"用户预支付成功",map);
            }
        } catch (Exception e1){
            System.out.println(e1.getMessage());
        }
        return new FormerResult(Constant.SUCCESS,Constant.NO,"预支付失败",PayConfig.getApp_appID());
    }

}
