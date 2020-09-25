package com.lrj.service.impl;

import com.lrj.VO.FormerResult;
import com.lrj.VO.OrderVo;
import com.lrj.VO.UserInfoVo;
import com.lrj.common.Constant;
import com.lrj.config.PayConfig;
import com.lrj.mapper.*;
import com.lrj.pojo.*;
import com.lrj.service.AppletsPayService;
import com.lrj.service.IOrderService;
import com.lrj.service.IUserService;
import com.lrj.service.RebateService;
import com.lrj.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.InetAddress;
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
    private IMerchantMapper merchantMapper;

    @Resource
    private ReservationMapper reservationMapper;

    @Resource
    private RebateService rebateService;

    @Resource
    private IOrderService orderService;

    @Resource
    private IUserService userService;
    @Resource
    private IItemJSONMapper itemJSONMapper;

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
                return appletsPay(request,order.getOrderNumber(),userBalance,order.getUserId());
            } else if (isBalance == 1&&order.getOrderType()== 2 || order.getOrderType()==4){
                return new FormerResult("",Fail_CODE,"余额支付只支持单项洗衣，单项家政",null);
            }else if (isBalance == 1&&order.getOrderType()== 1 || order.getOrderType()==3) {
                BalanceLog balanceLog = new BalanceLog();
                Balance balance = balanceMapper.selectByPrimaryKey(order.getUserId());
                //使用余额支付
                if (balance.getBalance().doubleValue() >= order.getTotalPrice().doubleValue()) {
                    userBalance =  balance.getBalance().doubleValue() - order.getTotalPrice().doubleValue();
                    double v = balance.getExpendAmount().doubleValue() + order.getTotalPrice().doubleValue();
                    balance.setBalance(new BigDecimal(userBalance)).setExpendAmount(new BigDecimal(v)).setLastModifyTime(DateUtils.formatDate(new Date()));
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
                    double v = balance.getExpendAmount().doubleValue() + balance.getBalance().doubleValue();
                    balance.setBalance(new BigDecimal("0.00")).setExpendAmount(new BigDecimal(v)).setLastModifyTime(DateUtils.formatDate(new Date()));
                    balanceMapper.updateByPrimaryKeySelective(balance);
                    balanceLog.setUserId(order.getUserId()).setType("2").setAmount(balance.getBalance()).
                            setCreateTime(DateUtils.formatDate(new Date()));
                    balanceLogMapper.insertSelective(balanceLog);
                    //添加流水记录
                    PayOperation payOperation = new PayOperation();
                    payOperation.setUserId(order.getUserId()).setTotalFee(balance.getBalance()).setOutTradeNo(orderNumber).
                            setUserPhone(order.getUserPhone()).setTradeSource(1).setTradeType(1).setTransactionId("余额支付+微信支付").
                            setCreateTime(DateUtils.formatDate(new Date()));
                    payOperationMapper.insertSelective(payOperation);
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
            if(PayConfig.getApplet_appID().equals(map.get("appid"))){
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
                        setTradeType(1).setBankType(String.valueOf(map.get("bank_type"))).setUserId(reOrder.getUserId()).setCreateTime(DateUtils.formatDate(new Date()));
                int i = payOperationMapper.insertSelective(payOperation);
                System.out.println("添加成功"+i);
                User user = userMapper.selectByPrimaryKey(reOrder.getUserId());
                //上级用户返利
                OrderVo orderVo = orderService.findOrderByOrderNumber(reOrder.getOrderNumber());
                rebateService.rebate(orderVo);
                /*if (user.getSuperType()==1&&reOrder.getOrderType()!=2&&reOrder.getOrderType()!=4) {
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
                if (user.getSuperType()==2) {
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
                }*/

                //更新预约表
                if (reOrder.getOrderType()!=2&&reOrder.getOrderType()!=4) {
                    Example example1 = new Example(Reservation.class);
                    example1.createCriteria().andEqualTo("orderNumber",reOrder.getOrderNumber());
                    List<Reservation> reservations = reservationMapper.selectByExample(example1);
                    if (reservations!=null||reservations.size()!=0) {
                        for (Reservation reservation : reservations) {
                            reservation.setStatus(3);
                            reservationMapper.updateByPrimaryKeySelective(reservation);
                        }
                    }
                }
                //推送服务订单通知
                jiGuangJPUSHPost.doPostForHelpStaff();
                //发送短信(订单支付成功（预约不触发，下单商品含袋洗不触发）)
                List<ItemJSON> itemJSONList = null;
                switch (orderVo.getOrderType()){
                    case 1:
                        itemJSONList = itemJSONMapper.getItemJSONByOrderNumberFromItemJSONOnly(orderVo.getOrderNumber());
                        break;
                    case 2:
                        itemJSONList = itemJSONMapper.getItemJSONByOrderNumber(orderVo.getOrderNumber());
                        break;
                    case 3:
                        itemJSONList = itemJSONMapper.getItemJSONByOrderNumberFromItemJSONOnly(orderVo.getOrderNumber());
                        break;
                    case 4:
                        itemJSONList = itemJSONMapper.getItemJSONByOrderNumber(orderVo.getOrderNumber());
                        break;
                }
                int count = 0;
                for (ItemJSON itemJSON : itemJSONList){
                    if (itemJSON.getItemId()==391 || itemJSON.getItemId()==393){
                        count+=1;
                    }
                }
                UserInfoVo userInfoVo = userService.findUserInfoByUserId(orderVo.getUserId());
                if(count ==0){
                    jiGuangSMSSend.sendSMS(userInfoVo.getUserPhone(),15542,184740,"");
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
                "b".equals(orderNumber.substring(0,1))||"c".equals(orderNumber.substring(0,1))||
                "d".equals(orderNumber.substring(0,1))||"e".equals(orderNumber.substring(0,1))) {
            addBalanceLog(orderNumber,userBalance,1,userId);
            addBalanceLog(orderNumber,userBalance,2,userId);
        }
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo(ORDER_NUMBER,orderNumber);
        List<Order> orders = orderMapper.selectByExample(example);
        for (Order order : orders) {
            reOrder = order;
        }
        Example example1 = new Example(User.class);
        example1.createCriteria().andEqualTo("userPhone",reOrder.getUserPhone());
        List<User> users = userMapper.selectByExample(example1);
        for (User user : users) {
            openId = user.getOpenId();
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
        HashMap<String, String> params = new HashMap<>();
        params.put("appid",PayConfig.getApplet_appID());
        params.put("attach","cheshi");
        params.put("mch_id",PayConfig.getMin_certPassword());
        params.put("nonce_str", StringUtils.generateNonceStr());
        params.put("body", "懒人家洗衣-订单支付");
        params.put("out_trade_no", orderNumber);
        params.put("total_fee", value);
        try {
            InetAddress localAddr = InetAddress.getLocalHost();
            String localIp = localAddr.getHostAddress().toString();
            params.put("spbill_create_ip", localIp); //终端IP
        }catch (Exception e1){
            System.out.println(e1.getMessage());
        }
        //params.put("spbill_create_ip", IpUtils.getIpAddr(request)); //终端IP
        params.put("notify_url", PayConfig.notify_url);  //异步通知回调地址
        params.put("trade_type", PayConfig.TRADETYPE); //支付类型
        params.put("sign_type","MD5");
        params.put("openid",openId);
        //将非空参数进行签名运算(排序，MD5加密)

        //String result = HttpClientTool.doPostByXml(url, xml);
        //调用对方demo请求下单
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        String responseContent = "";
        try {
            String sign = WXPayUtil.generateSignature(params,PayConfig.getApi_key(), WXPayUtil.SignType.MD5);
            System.out.println("第一次签名"+sign);
            log.info(sign);
            params.put("sign", sign);
            //将请求参数转化为微信支付要求的xml格式文件
            String xml = WXPayUtil.mapToXml1(params);
            System.out.println(xml);
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(xml,"UTF-8"));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            System.out.println(responseContent);
            response.close();
            httpClient.close();

        } catch (IOException e2) {
            System.out.println(e2.getMessage());
            new FormerResult("success", 1, "网络请求异常，请稍后重试或联系客服！", null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(response, httpClient);
        }

        //将String转化成map
        try {
            Map resultMap = WXPayUtil.xmlToMap(responseContent);
            System.out.println(resultMap.toString());
            //请求返回结果的处理
            String returnCode = (String) resultMap.get("return_code");
            String resultCode = (String)resultMap.get("result_code");
            if ("SUCCESS".equals(returnCode) && returnCode.equals(resultCode)){
                HashMap<String, String> map = new HashMap<>();
                map.put("appId", PayConfig.getApplet_appID());
                map.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
                //这边的随机字符串必须是第一次生成sign时，微信返回的随机字符串，不然小程序支付时会报签名错误
                map.put("nonceStr", String.valueOf(resultMap.get("nonce_str")));
                map.put("package", "prepay_id=" + resultMap.get("prepay_id"));
                map.put("signType", PayConfig.SIGNTYPE);
                //String paySign = WXPayUtil.getSign(map, PayConfig.getApi_key());
                String paySign=WXPayUtil.generateSignature(map,PayConfig.getApi_key(), WXPayUtil.SignType.MD5);
                System.out.println("第二次签名"+paySign);
                log.info(paySign);
                map.put("paySign",paySign);
                System.out.println(map.toString());
                return new FormerResult(Constant.SUCCESS,Constant.YES,"用户预支付成功",map);
            }
        } catch (Exception e1){
            System.out.println(e1.getMessage());
        }
        return new FormerResult(Constant.SUCCESS,Constant.NO,"预支付失败",PayConfig.getApp_appID());
    }

    private void addBalanceLog(String orderNumber, double userBalance, Integer typepe, Integer userId) {
        BalanceLog balanceLog = new BalanceLog();
        Balance balance = balanceMapper.selectByPrimaryKey(userId);
        switch (orderNumber.substring(0,1)) {
            case "a":
                balanceLog.setUserId(userId).setCreateTime(DateUtils.formatDate(new Date()));
                if (typepe == 1) {
                    balanceLog.setAmount(new BigDecimal(userBalance)).setType("1").setStatus(1).setRechargeOrderNumber(orderNumber);
                    double v = balance.getBalance().doubleValue() + userBalance;
                    double v1 = balance.getTopUpAmount().doubleValue() + userBalance;
                    balance.setBalance(new BigDecimal(v)).setTopUpAmount(new BigDecimal(v1)).setLastModifyTime(DateUtils.formatDate(new Date()));
                } else if (typepe == 2) {
                    balanceLog.setAmount(new BigDecimal(userBalance * 0.2)).setType("4").setStatus(1).setRechargeOrderNumber(orderNumber);
                    double v = balance.getBalance().doubleValue() + userBalance*0.2;
                    double v1 = balance.getTopUpAmount().doubleValue() + userBalance*0.2;
                    balance.setBalance(new BigDecimal(v)).setTopUpAmount(new BigDecimal(v1)).setLastModifyTime(DateUtils.formatDate(new Date()));
                }
                balanceMapper.updateByPrimaryKeySelective(balance);
                balanceLogMapper.insertSelective(balanceLog);
                break;
            case "b":
                balanceLog.setUserId(userId).setCreateTime(DateUtils.formatDate(new Date()));
                if (typepe == 1) {
                    double v = balance.getBalance().doubleValue() + userBalance;
                    double v1 = balance.getTopUpAmount().doubleValue() + userBalance;
                    balance.setBalance(new BigDecimal(v)).setTopUpAmount(new BigDecimal(v1)).setLastModifyTime(DateUtils.formatDate(new Date()));
                    balanceLog.setAmount(new BigDecimal(userBalance)).setType("1").setStatus(1).setRechargeOrderNumber(orderNumber);
                } else if (typepe == 2) {
                    double v = balance.getBalance().doubleValue() + userBalance*0.25;
                    double v1 = balance.getTopUpAmount().doubleValue() + userBalance*0.25;
                    balance.setBalance(new BigDecimal(v)).setTopUpAmount(new BigDecimal(v1)).setLastModifyTime(DateUtils.formatDate(new Date()));
                    balanceLog.setAmount(new BigDecimal(userBalance * 0.25)).setType("4").setStatus(1).setRechargeOrderNumber(orderNumber);
                }
                balanceMapper.updateByPrimaryKeySelective(balance);
                balanceLogMapper.insertSelective(balanceLog);
                break;
            case "c":
                balanceLog.setUserId(userId).setCreateTime(DateUtils.formatDate(new Date()));
                if (typepe == 1) {
                    double v = balance.getBalance().doubleValue() + userBalance;
                    double v1 = balance.getTopUpAmount().doubleValue() + userBalance;
                    balance.setBalance(new BigDecimal(v)).setTopUpAmount(new BigDecimal(v1)).setLastModifyTime(DateUtils.formatDate(new Date()));
                    balanceLog.setAmount(new BigDecimal(userBalance)).setType("1").setStatus(1).setRechargeOrderNumber(orderNumber);
                } else if (typepe == 2) {
                    double v = balance.getBalance().doubleValue() + userBalance*0.3;
                    double v1 = balance.getTopUpAmount().doubleValue() + userBalance*0.3;
                    balance.setBalance(new BigDecimal(v)).setTopUpAmount(new BigDecimal(v1)).setLastModifyTime(DateUtils.formatDate(new Date()));
                    balanceLog.setAmount(new BigDecimal(userBalance * 0.3)).setType("4").setStatus(1).setRechargeOrderNumber(orderNumber);
                }
                balanceMapper.updateByPrimaryKeySelective(balance);
                balanceLogMapper.insertSelective(balanceLog);
                break;
            case "d":
                balanceLog.setUserId(userId).setCreateTime(DateUtils.formatDate(new Date()));
                if (typepe == 1) {
                    double v = balance.getBalance().doubleValue() + userBalance;
                    double v1 = balance.getTopUpAmount().doubleValue() + userBalance;
                    balance.setBalance(new BigDecimal(v)).setTopUpAmount(new BigDecimal(v1)).setLastModifyTime(DateUtils.formatDate(new Date()));
                    balanceLog.setAmount(new BigDecimal(userBalance)).setType("1").setStatus(1).setRechargeOrderNumber(orderNumber);
                } else if (typepe == 2) {
                    double v = balance.getBalance().doubleValue() + userBalance*0.4;
                    double v1 = balance.getTopUpAmount().doubleValue() + userBalance*0.4;
                    balance.setBalance(new BigDecimal(v)).setTopUpAmount(new BigDecimal(v1)).setLastModifyTime(DateUtils.formatDate(new Date()));
                    balanceLog.setAmount(new BigDecimal(userBalance * 0.4)).setType("4").setStatus(1).setRechargeOrderNumber(orderNumber);
                }
                balanceMapper.updateByPrimaryKeySelective(balance);
                balanceLogMapper.insertSelective(balanceLog);
                break;
            default:
                if (typepe == 1&& "e".equals(orderNumber.substring(0, 1))) {
                    double v = balance.getBalance().doubleValue() + userBalance;
                    double v1 = balance.getTopUpAmount().doubleValue() + userBalance;
                    balance.setBalance(new BigDecimal(v)).setTopUpAmount(new BigDecimal(v1)).setLastModifyTime(DateUtils.formatDate(new Date()));
                    balanceLog.setUserId(userId).setAmount(new BigDecimal(userBalance)).setType("1").setStatus(1).setRechargeOrderNumber(orderNumber).setCreateTime(DateUtils.formatDate(new Date()));
                    balanceMapper.updateByPrimaryKeySelective(balance);
                    balanceLogMapper.insertSelective(balanceLog);
                    break;
                }
        }
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
