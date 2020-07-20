package com.lrj.service.impl;

import com.lrj.VO.FormerResult;
import com.lrj.common.Constant;
import com.lrj.config.PayConfig;
import com.lrj.mapper.*;
import com.lrj.pojo.*;
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
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lrj.VO.FormerResult.Fail_CODE;
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

    @Resource
    private BalanceLogMapper balanceLogMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserLevelMapper userLevelMapper;

    @Resource
    private AppRebateMapper appRebateMapper;

    @Resource
    private MerchantMapper merchantMapper;

    @Resource
    private ReservationMapper reservationMapper;

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
            if (isBalance == 0&&order.getOrderType()==2||order.getOrderType()==4) {
                return appletsPay(request,order.getOrderNumber(),userBalance,order.getUserId());
            } else if (isBalance == 1&&order.getOrderType()== 1 || order.getOrderType()==3) {
                BalanceLog balanceLog = new BalanceLog();
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
                    balanceLog.setUserId(order.getUserId()).setType("2").setAmount(order.getTotalPrice()).
                            setCreateTime(DateUtils.formatDate(new Date()));
                    balanceLogMapper.insertSelective(balanceLog);
                    return new FormerResult(Constant.SUCCESS,Constant.YES,"余额支付成功",null);
                } else {
                    //用户余额小于订单总价
                    userBalance =  order.getTotalPrice().doubleValue() - balance.getBalance().doubleValue();
                    //更新用户余额
                    balance.setBalance(new BigDecimal("0.00"));
                    balanceMapper.updateByPrimaryKeySelective(balance);
                    balanceLog.setUserId(order.getUserId()).setType("2").setAmount(balance.getBalance()).
                            setCreateTime(DateUtils.formatDate(new Date()));
                    balanceLogMapper.insertSelective(balanceLog);
                    //剩余价格微信支付
                    return appletsPay(request, orderNumber,userBalance,order.getUserId());
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
                System.out.println("添加成功"+i);
                User user = userMapper.selectByPrimaryKey(reOrder.getUserId());
                //上级用户返利
                if (user.getSuperType()!=null&&user.getSuperType()==1&&reOrder.getOrderType()!=2&&reOrder.getOrderType()!=4) {
                    UserLevel userLevel = userLevelMapper.selectByPrimaryKey(user.getSuperId());
                    if (userLevel.getLevelId()!=1) {
                        AppRebate appRebate = new AppRebate();
                        appRebate.setUserId(user.getSuperId()).setLowId(user.getAppUserId()).setOrderNumber(reOrder.getOrderNumber()).setType(1).
                                setSource("下级返利").setCreateTime(DateUtils.formatDate(new Date()));
                        switch (userLevel.getLevelId()){
                            case 2:
                                appRebate.setBackMoney(BigDecimal.valueOf(reOrder.getTotalPrice().doubleValue() * 0.01));
                                appRebateMapper.insertSelective(appRebate);
                                break;
                            case 3:
                                appRebate.setBackMoney(BigDecimal.valueOf(reOrder.getTotalPrice().doubleValue() * 0.05));
                                appRebateMapper.insertSelective(appRebate);
                                break;
                            default:
                                appRebate.setBackMoney(BigDecimal.valueOf(reOrder.getTotalPrice().doubleValue() * 0.1));
                                appRebateMapper.insertSelective(appRebate);
                                break;
                        }
                    }
                }
                //商家返利
                if (user.getSuperType()!=null&&user.getSuperType()==2) {
                    Merchant merchant = merchantMapper.selectByPrimaryKey(user.getSuperId());
                    AppRebate appRebate = new AppRebate();
                    appRebate.setUserId(merchant.getMerchantId()).setLowId(user.getAppUserId()).setOrderNumber(reOrder.getOrderNumber()).setType(2).
                            setCreateTime(DateUtils.formatDate(new Date()));
                    if (reOrder.getOrderType()==1||reOrder.getOrderType()==3) {
                        appRebate.setBackMoney(BigDecimal.valueOf(reOrder.getTotalPrice().doubleValue() * merchant.getDistributionRatio().doubleValue()));
                    }else if (reOrder.getOrderType()==2||reOrder.getOrderType()==4){
                        appRebate.setBackMoney(BigDecimal.valueOf(reOrder.getTotalPrice().doubleValue() * 0.05));
                    }
                    appRebateMapper.insertSelective(appRebate);
                }
                //更新预约表
                Example example1 = new Example(Reservation.class);
                example1.createCriteria().andEqualTo("orderNumber",reOrder.getOrderNumber());
                List<Reservation> reservations = reservationMapper.selectByExample(example1);
                if (reservations!=null||reservations.size()!=0) {
                    for (Reservation reservation : reservations) {
                        reservation.setStatus(3);
                        reservationMapper.updateByPrimaryKeySelective(reservation);
                    }
                }
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
     * @param: request
     * @Description: 余额充值
     * @Author: LxH
     * @Date: 2020/6/30 17:31
     */
    @Override
    public FormerResult balanceTopUp(HttpServletRequest request) {
        Integer userId = Integer.valueOf(request.getParameter("userId"));
        Integer rechargeType = Integer.valueOf(request.getParameter("rechargeType"));
        /** 校验必须参数 **/
        if (userId == 0||userId==null || rechargeType == 0||rechargeType==null) {
            return new FormerResult("success", 1, "参数有误,请检查参数", null);
        }
        String orderNumber = "";
        switch (rechargeType){
            //充值100 送20%余额
            case 1:
                orderNumber = "a"+RandomUtil.generateRandomNumberString(13);
                return appletsPay(request,orderNumber,100.00,userId);
            //充值300 送25%余额
            case 2:
                orderNumber = "b"+RandomUtil.generateRandomNumberString(13);
                return appletsPay(request,orderNumber,300.00,userId);
            //充值500 送30%余额
            case 3:
                orderNumber = "c"+RandomUtil.generateRandomNumberString(13);
                return appletsPay(request,orderNumber,500.00,userId);
            //充值1000 送40%余额
            case 4:
                orderNumber = "d"+RandomUtil.generateRandomNumberString(13);
                return appletsPay(request,orderNumber,1000.00,userId);
            //自定义充值   不送
            case 5:
                orderNumber = "e"+RandomUtil.generateRandomNumberString(13);
                BigDecimal payMoney = new BigDecimal(request.getParameter("payMoney"));
                return appletsPay(request,orderNumber,payMoney.doubleValue(),userId);
            default:
                return new FormerResult().setErrorCode(Fail_CODE).setErrorTip("rechargeType值不可用");
        }
    }

    /**
     * @Description: 微信预支付
     * @Author: LxH
     * @Date: 2020/6/24 17:12
     */
    private FormerResult appletsPay(HttpServletRequest request,String orderNumber, double userBalance,Integer userId) {
        Order reOrder = new Order();
        String value;
        String openId = "";
        //添加余额记录
        if ("a".equals(orderNumber.substring(0,1)) ||
                "b".equals(orderNumber)||"c".equals(orderNumber)||
                "d".equals(orderNumber)||"e".equals(orderNumber)) {
            addBalanceLog(orderNumber,userBalance,1,userId);
        }
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
        params.put("total_fee", "1");
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

    private void addBalanceLog(String orderNumber, double userBalance, Integer typepe, Integer userId) {
        BalanceLog balanceLog = new BalanceLog();
        switch (orderNumber.substring(0,1)) {
            case "a":
                balanceLog.setUserId(userId).setCreateTime(DateUtils.formatDate(new Date()));
                if (typepe == 1) {
                    balanceLog.setAmount(new BigDecimal(userBalance)).setType("1");
                } else if (typepe == 2) {
                    balanceLog.setAmount(new BigDecimal(userBalance * 0.2)).setType("4");
                }
                balanceLogMapper.insertSelective(balanceLog);
                break;
            case "b":
                balanceLog.setUserId(userId).setCreateTime(DateUtils.formatDate(new Date()));
                if (typepe == 1) {
                    balanceLog.setAmount(new BigDecimal(userBalance)).setType("1");
                } else if (typepe == 2) {
                    balanceLog.setAmount(new BigDecimal(userBalance * 0.25)).setType("4");
                }
                balanceLogMapper.insertSelective(balanceLog);
                break;
            case "c":
                balanceLog.setUserId(userId).setCreateTime(DateUtils.formatDate(new Date()));
                if (typepe == 1) {
                    balanceLog.setAmount(new BigDecimal(userBalance)).setType("1");
                } else if (typepe == 2) {
                    balanceLog.setAmount(new BigDecimal(userBalance * 0.3)).setType("4");
                }
                balanceLogMapper.insertSelective(balanceLog);
                break;
            case "d":
                balanceLog.setUserId(userId).setCreateTime(DateUtils.formatDate(new Date()));
                if (typepe == 1) {
                    balanceLog.setAmount(new BigDecimal(userBalance)).setType("1");
                } else if (typepe == 2) {
                    balanceLog.setAmount(new BigDecimal(userBalance * 0.4)).setType("4");
                }
                balanceLogMapper.insertSelective(balanceLog);
                break;
            default:
                if (typepe == 1&& "e".equals(orderNumber.substring(0, 1))) {
                    balanceLog.setUserId(userId).setAmount(new BigDecimal(userBalance)).setType("1").setCreateTime(DateUtils.formatDate(new Date()));
                    break;
                }
        }
    }
}
