package com.lrj.service.impl;

import com.lrj.VO.AliPayVo;
import com.lrj.mapper.IPayMapper;
import com.lrj.pojo.BalanceRecord;
import com.lrj.pojo.PayOperation;
import com.lrj.service.IOrderService;
import com.lrj.service.IPayService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;

import java.util.Map;

@Service
@Transactional
public class PayServiceImpl implements IPayService {

    @Resource
    private IPayMapper payMapper;
    /**
     * 支付宝提供给商户的服务接入网关URL(新)
     */
    private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?service=notify_verify&";


    @Resource
    private IOrderService orderService;

    public String wxminiPayNotify(String xml) throws Exception {
        return null;
    }

    public String appAliPayNotify(AliPayVo aliPayVo, Map parameterMap) throws Exception {
        return null;
    }

    public String appWXPayNotify(String xml) throws Exception {
        return null;
    }

    @Override
    public void payFlowRecord(PayOperation payOperation) {
        payMapper.payFlowRecord(payOperation);
    }

    @Override
    public Integer userWithdrawApply(PayOperation payOperation) {
        return payMapper.userWithdrawApply(payOperation);
    }

    @Override
    public void addUserBalanceRecord(BalanceRecord balanceRecord) {
         payMapper.addUserBalanceRecord(balanceRecord);
    }

    @Override
    public void updateUserBalanceRecord(String rechargeOrderNumber) {
        payMapper.updateUserBalanceRecord(rechargeOrderNumber);
    }

    public void WXPayFlowRecord(Map<String, Object> flowRecordMap) {
        payMapper.WXPayFlowRecord(flowRecordMap);
    }

    /**
     * 微信支付回调
     *
     * @param xml 微信支付参数
     * @return
     */
    /*public String wxminiPayNotify(String xml) throws Exception {
        // 校验签名
        WXPay pay = new WXPay(new MyWXPayConfig(), Messages.getString("wx_notify_url"));
        boolean result = pay.validatePayResult(xml);
        if (!result) {
            return "xx";
        }
        String resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";
        // 封装成对象
        NotifyData payResData = (NotifyData) Util.getObjectFromXML(xml, NotifyData.class);
        Long orderNo = Long.parseLong(payResData.getOut_trade_no());
        log.info("微信小程序支付订单号为 : " + orderNo);
        // 判断是否已处理
        AppPaymentEntity appPaymentEntity = (AppPaymentEntity) sqlMapClientTemplate
                .queryForObject("selectAppPaymentEntityById", orderNo);
        if (appPaymentEntity != null) {
            log.info("微信小程序已经支付过了支付");
            return resXml;
        }
        // 取出月卡充值记录
        UserMonthCardRecord record = new UserMonthCardRecord();
        record.setOrderNo(orderNo);
        record = (UserMonthCardRecord) sqlMapClientTemplate.queryForObject("wxMonthCard.selectRecord", record);
        // 校验支付金额
        if (new BigDecimal(payResData.getTotal_fee()).compareTo(record.getPrice().multiply(new BigDecimal("100"))) != 0) {
            log.info("支付金额不匹配");
            throw new Exception("支付金额不匹配!");
        }
        // 执行业务操作
        AppPaymentEntity paymentEntity = new AppPaymentEntity();
        paymentEntity.setOrderId(orderNo);
        paymentEntity.setTradeNo(payResData.getTransaction_id());
        paymentEntity.setTotalFee(payResData.getTotal_fee());
        paymentEntity.setTradeStatus(payResData.getResult_code());
        paymentEntity.setSellerEmail(payResData.getMch_id());
        paymentEntity.setBuyerEmail(payResData.getOpenid());
        paymentEntity.setPlatform("微信小程序月卡-充值");
        paymentEntity.setCreateTime(payResData.getTime_end());
        *//** 保存支付记录 **//*
        sqlMapClientTemplate.insert("insertAppPaymentEntity", paymentEntity);


        // 取出用户要购买的月卡
        BuyCardOption option = new BuyCardOption();
        option.setWxMonthCardId(record.getWxMonthCardId());
        WxMonthCard card = (WxMonthCard) sqlMapClientTemplate.queryForObject("wxMonthCard.selectAll", option);

        // 取出用户的月卡
        UserWxMonthCardVo userWxMonthCardVo = (UserWxMonthCardVo) sqlMapClientTemplate
                .queryForObject("wxMonthCard.selectUserMonthCard", record.getUserId());

        boolean insert = false;
        if (userWxMonthCardVo == null) {
            insert = true;
            userWxMonthCardVo = new UserWxMonthCardVo();
            userWxMonthCardVo.setUserId(record.getUserId());
            userWxMonthCardVo.setStartTime(new Date());
            userWxMonthCardVo.setIsAuto(2);
        } else {
            userWxMonthCardVo.setStartTime(new Date());
        }
        WxMonthCard wxMonthCard = (WxMonthCard) sqlMapClientTemplate.queryForObject("wxMonthCard.selectWxUserCardId",  card.getId());
        // 判断用户的月卡是否已过期
        if (userWxMonthCardVo.getEndTime() == null ||
                userWxMonthCardVo.getEndTime().getTime() < System.currentTimeMillis()  || userWxMonthCardVo.getmUsedCount().equals(userWxMonthCardVo.getmTotalCount())) {
            log.info("微信次奥程序在支付，月卡不存在或过期");
            // 如果已经过期啦，重新添加月卡
            userWxMonthCardVo.setStatus((byte) 1);
            Date date = new Date();
            int days = wxMonthCard.getExpire() * 30;
//            userWxMonthCardVo.setEndTime(DateUtils.addMonths(date, 1));
            userWxMonthCardVo.setEndTime(DateUtils.addDays(date, days));
            userWxMonthCardVo.setOptionTime(record.getOptionTime());
            userWxMonthCardVo.setUsedCount(0);
            userWxMonthCardVo.setTotalCount(card.getCount());
            userWxMonthCardVo.setmUsedCount(0);
            userWxMonthCardVo.setmTotalCount(card.getCount());
            userWxMonthCardVo.setWxMonthCardId(record.getWxMonthCardId());
            userWxMonthCardVo.setAddressId(record.getAddressId());
            // 添加
            if (insert) {
                sqlMapClientTemplate.insert("wxMonthCard.insertUserMonthCard", userWxMonthCardVo);
            } else {
                sqlMapClientTemplate.update("wxMonthCard.updateUserMonthCard", userWxMonthCardVo);
            }
        } else { // 如果月卡没有过期
            // 未过期的不做处理，只保存支付状态，可以在后台手动退款
            log.info("微信小程序支付，月卡没有过期");
            doDiffCardPay(record, orderNo, (byte) 3,(byte)1);
            return resXml;
            // 判断当前的月卡和购买的月卡是不是同一个
//            if (!card.getId().equals(record.getWxMonthCardId())) {
//                log.info("微信小程序支付，购买的月卡和当前的月卡不是同一个");
//                return resXml;
//            }
//            // 如果是同一个月卡
//            Date endTime = userWxMonthCardVo.getEndTime();
//            // 截止日期天机一个月
//            userWxMonthCardVo.setEndTime(DateUtils.addMonths(endTime, 1));
//            // 总的可用次数增加
//            userWxMonthCardVo.setTotalCount(userWxMonthCardVo.getTotalCount() + card.getCount());
//            if (record.getAddressId() != null) {
//                userWxMonthCardVo.setAddressId(record.getAddressId());
//            }
//            sqlMapClientTemplate.update("wxMonthCard.updateUserMonthCard", userWxMonthCardVo);
        }

        // 更新月卡充值记录为已完成
        doDiffCardPay(record, orderNo, (byte) 2,(byte)1);
        if (wxMonthCard.getCardType() != null) {
            if (wxMonthCard.getCardType().intValue() == 1) {
                appOrderService.calculateEarnings(orderNo.toString(), 2);
            }
        }
        log.info("微信小程序支付--成功!");

//        String text = "尊敬的懒人家用户，您已成功购买懒人家，使用期限为#count#天，#date#到期，使用次数为#number#次，" +
//                "如有任何疑问，咨询客服，客服电话0871-65628435，祝您生活愉快！";
        return resXml;
    }

    private void doDiffCardPay(UserMonthCardRecord record, Long orderNo, byte status,Byte payWay) throws Exception {
        // 更新月卡充值记录为已完成

        record.setOrderNo(orderNo);
        record.setStatus(status);
        record.setStartTime(new Date());
        record.setEndTime(DateUtils.addMonths(new Date(), 1));
        record.setPayTime(new Date());
        record.setPayWay(payWay);

        int count = sqlMapClientTemplate.update("wxMonthCard.updateRecord", record);
        if (count == 0) {
            log.info("微信小程序更新状态失败");
            throw new Exception("更新状态失败!");
        }

    }


    *//**
     * app月卡购买支付宝回调
     *
     * @param aliPayVo
     * @param parameterMap
     * @return
     *//*
    @Override
    public String appAliPayNotify(AliPayVo aliPayVo, Map parameterMap) throws Exception {
        //校验订单号
        Long orderNo = Long.parseLong(aliPayVo.getOut_trade_no());
        // 取出月卡充值记录
        UserMonthCardRecord record = new UserMonthCardRecord();
        record.setOrderNo(orderNo);
        record = (UserMonthCardRecord) sqlMapClientTemplate.queryForObject("wxMonthCard.selectRecord", record);
        // 校验金额
        BigDecimal price = record.getPrice();
        if (price.compareTo(new BigDecimal(aliPayVo.getTotal_fee())) != 0) {
            log.info("支付金额错误");
            throw new Exception("支付金额不匹配!");
        }
        // 支付宝相关校验
        int i = checkAliPayBaseInfo(aliPayVo, parameterMap, orderNo);
        if (i == 0) {
            // 取出用户要购买的月卡
            BuyCardOption option = new BuyCardOption();
            option.setWxMonthCardId(record.getWxMonthCardId());
            WxMonthCard card = (WxMonthCard) sqlMapClientTemplate.queryForObject("wxMonthCard.selectAll", option);

            // 取出用户的月卡
            UserWxMonthCardVo userWxMonthCardVo = (UserWxMonthCardVo) sqlMapClientTemplate
                    .queryForObject("wxMonthCard.selectUserMonthCard", record.getUserId());

            boolean insert = false;
            if (userWxMonthCardVo == null) {
                insert = true;
                userWxMonthCardVo = new UserWxMonthCardVo();
                userWxMonthCardVo.setUserId(record.getUserId());
                userWxMonthCardVo.setStartTime(new Date());
                userWxMonthCardVo.setIsAuto(2);
            } else {
                userWxMonthCardVo.setStartTime(new Date());
            }
            WxMonthCard wxMonthCard = (WxMonthCard) sqlMapClientTemplate.queryForObject("wxMonthCard.selectWxUserCardId",  card.getId());
            // 判断用户的月卡是否已过期
            if (userWxMonthCardVo.getEndTime() == null ||
                    userWxMonthCardVo.getEndTime().getTime() < System.currentTimeMillis() || userWxMonthCardVo.getmUsedCount().equals(userWxMonthCardVo.getmTotalCount())) {
                log.info("微信次奥程序在支付，月卡不存在或过期");
                // 如果已经过期啦，重新添加月卡
                userWxMonthCardVo.setStatus((byte) 1);
                Date date = new Date();
//                userWxMonthCardVo.setEndTime(DateUtils.addMonths(date, 1));
                int days = wxMonthCard.getExpire() * 30;
                userWxMonthCardVo.setEndTime(DateUtils.addDays(date, days));
                userWxMonthCardVo.setOptionTime(record.getOptionTime());
                userWxMonthCardVo.setUsedCount(0);
                userWxMonthCardVo.setTotalCount(card.getCount());
                userWxMonthCardVo.setmUsedCount(0);
                userWxMonthCardVo.setmTotalCount(card.getCount());
                userWxMonthCardVo.setWxMonthCardId(record.getWxMonthCardId());
                userWxMonthCardVo.setAddressId(record.getAddressId());
                // 添加
                if (insert) {
                    sqlMapClientTemplate.insert("wxMonthCard.insertUserMonthCard", userWxMonthCardVo);
                } else {
                    sqlMapClientTemplate.update("wxMonthCard.updateUserMonthCard", userWxMonthCardVo);
                }
            } else { // 如果月卡没有过期
                // 未过期的不做处理，只保存支付状态，可以在后台手动退款
                log.info("支付宝支付，月卡没有过期");
                doDiffCardPay(record, orderNo, (byte) 3,(byte)3);
            }

            // 更新月卡充值记录为已完成
            doDiffCardPay(record, orderNo, (byte) 2,(byte)3);

            if (wxMonthCard.getCardType() != null) {
                if (wxMonthCard.getCardType().intValue() == 1) {
                    appOrderService.calculateEarnings(orderNo.toString(), 2);
                }
            }
            log.info("微信小程序支付--成功!");

        } else if (i == 2) {
            return "ERROR";
        }
        return "SUCCESS";

    }


    *//**
     * 支付宝回调校验
     *
     * @param alipayVo
     * @param map
     * @return 0-校验成功，1-已支付过，2-错误
     * @author linqin
     * @date 2018/6/8
     *//*
    public int checkAliPayBaseInfo(AliPayVo alipayVo, Map map, Long orderNo) {
        *//** 判断是否已处理 **//*
        AppPaymentEntity appPaymentEntity = (AppPaymentEntity) sqlMapClientTemplate.queryForObject("selectAppPaymentEntityById", Long.parseLong(alipayVo.getOut_trade_no()));
        *//** 如果后台存在支付记录则直接返回 **//*
        if (appPaymentEntity != null) {
            return 1;
        }
        *//** 实体转MAP **//*
        Map<String, String> Params = BeanUtils.beanToMap(alipayVo);
        *//** 过滤空值、sign与sign_type参数 **//*
        Map<String, String> sParaNew = AlipayCore.paraFilter(Params);
        *//** 获取待签名字符串 **//*
        String preSignStr = AlipayCore.createLinkString(sParaNew);
        *//** 获得签名验证结果 **//*
        boolean isSign = false;
        *//** RSA加密方式 **//*
        if ("RSA".equals(AlipayConfig.sign_type)) {
            *//** 验签 **//*
            isSign = RSA.verify(preSignStr, alipayVo.getSign(), AlipayConfig.ali_public_key, AlipayConfig.input_charset);
        }
        *//** 验签正确以后,验证是否是支付宝发来的通知 **//*
        if (isSign) {
            Map<String, String> createMap = new HashMap<String, String>();
            createMap.put("partner", AlipayConfig.partner);
            createMap.put("notify_id", alipayVo.getNotify_id());
            *//** 通知正确以后验证支付状态及订单号 **//*
            String isAlipayInform = HttpClientUtil.doPost(ALIPAY_GATEWAY_NEW, createMap, AlipayConfig.input_charset);
            System.out.println("回调 = " + isAlipayInform);
            if ("true".equalsIgnoreCase(isAlipayInform)) {
                if ("TRADE_SUCCESS".equals(alipayVo.getTrade_status())
                        && alipayVo.getOut_trade_no() != null) {
                    *//** 支付记录实体生成 **//*
                    AppPaymentEntity paymentEntity = new AppPaymentEntity();
                    *//** 订单号 **//*
                    paymentEntity.setOrderId(Long.parseLong(alipayVo.getOut_trade_no()));
                    *//** 交易流水 **//*
                    paymentEntity.setTradeNo(alipayVo.getTrade_no());
                    *//** 总价 **//*
                    paymentEntity.setTotalFee(alipayVo.getTotal_fee());
                    *//** 交易成功状态 **//*
                    paymentEntity.setTradeStatus(alipayVo.getTrade_status());
                    *//** 卖家账号 **//*
                    paymentEntity.setSellerEmail(alipayVo.getSeller_email());
                    *//** 买家账号 **//*
                    paymentEntity.setBuyerEmail(alipayVo.getBuyer_email());
                    *//** 平台及业务标识 **//*
                    paymentEntity.setPlatform("支付宝");
                    *//** 支付时间 **//*
                    paymentEntity.setCreateTime(alipayVo.getGmt_payment());
                    *//** 判断不同的业务,调用不同的service **//*

                    return 0;
                }
            }
        }
        return 2;
    }


    *//**
     * app月卡购买微信支付回调
     *
     * @param xml
     * @return
     * @throws IOException
     *//*
    @Override
    public String appWXPayNotify(String xml) throws Exception {
        *//** 支付成功微信服务器通知XML **//*
        String resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
        *//** 获取微信服务请求XML参数 **//*
        //获取微信服务请求xml参数
        *//** XML转对象 **//*
        log.info("微信商品读不到{}", xml);
        *//** 封装成对象 **//*
        NotifyData payResData = (NotifyData) Util.getObjectFromXML(xml, NotifyData.class);
        //校验订单号
        Long orderNo = Long.parseLong(payResData.getOut_trade_no());
        log.info("微信小程序支付订单号为 : " + orderNo);
        *//** 判断是否已处理 **//*
        AppPaymentEntity appPaymentEntity = (AppPaymentEntity) sqlMapClientTemplate.queryForObject("selectAppPaymentEntityById", Long.parseLong(payResData.getOut_trade_no()));
        *//** 如果已有支付记录直接返回 **//*
        if (appPaymentEntity != null) {
            return resXml;
        }
        *//** 验证签名是否合法 **//*
        Boolean isSign = Signature.checkIsSignValidFromResponseString(xml);
        *//** 校验通过 **//*
        if (isSign) {
            basePay(resXml,orderNo,payResData);
        }
       return "ERROR";

    }




    public String basePay(String resXml,Long orderNo,NotifyData payResData) throws Exception {
        // 取出月卡充值记录
        UserMonthCardRecord record = new UserMonthCardRecord();
        record.setOrderNo(orderNo);
        record = (UserMonthCardRecord) sqlMapClientTemplate.queryForObject("wxMonthCard.selectRecord", record);
        // 校验支付金额
        if (new BigDecimal(payResData.getTotal_fee()).compareTo(record.getPrice().multiply(new BigDecimal("100"))) != 0) {
            log.info("支付金额不匹配");
            throw new Exception("支付金额不匹配!");
        }
        // 执行业务操作
        AppPaymentEntity paymentEntity = new AppPaymentEntity();
        paymentEntity.setOrderId(orderNo);
        paymentEntity.setTradeNo(payResData.getTransaction_id());
        paymentEntity.setTotalFee(payResData.getTotal_fee());
        paymentEntity.setTradeStatus(payResData.getResult_code());
        paymentEntity.setSellerEmail(payResData.getMch_id());
        paymentEntity.setBuyerEmail(payResData.getOpenid());
        paymentEntity.setPlatform("微信小程序月卡-充值");
        paymentEntity.setCreateTime(payResData.getTime_end());
        *//** 保存支付记录 **//*
        sqlMapClientTemplate.insert("insertAppPaymentEntity", paymentEntity);


        // 取出用户要购买的月卡
        BuyCardOption option = new BuyCardOption();
        option.setWxMonthCardId(record.getWxMonthCardId());
        WxMonthCard card = (WxMonthCard) sqlMapClientTemplate.queryForObject("wxMonthCard.selectAll", option);
        // 取出用户的月卡
        UserWxMonthCardVo userWxMonthCardVo = (UserWxMonthCardVo) sqlMapClientTemplate
                .queryForObject("wxMonthCard.selectUserMonthCard", record.getUserId());

        boolean insert = false;
        if (userWxMonthCardVo == null) {
            insert = true;
            userWxMonthCardVo = new UserWxMonthCardVo();
            userWxMonthCardVo.setUserId(record.getUserId());
            userWxMonthCardVo.setStartTime(new Date());
//            userWxMonthCardVo.setWxMonthCardId(card.getId());
            userWxMonthCardVo.setIsAuto(2);
        } else {
            userWxMonthCardVo.setStartTime(new Date());
        }
        WxMonthCard wxMonthCard = (WxMonthCard) sqlMapClientTemplate.queryForObject("wxMonthCard.selectWxUserCardId", card.getId());
        // 判断用户的月卡是否已过期
        if (userWxMonthCardVo.getEndTime() == null ||
                userWxMonthCardVo.getEndTime().getTime() < System.currentTimeMillis() || userWxMonthCardVo.getmUsedCount().equals(userWxMonthCardVo.getmTotalCount())) {
            log.info("微信次奥程序在支付，月卡不存在或过期");
            // 如果已经过期啦，重新添加月卡
            userWxMonthCardVo.setStatus((byte) 1);
            Date date = new Date();
//            userWxMonthCardVo.setEndTime(DateUtils.addMonths(date, 1));
            int days = wxMonthCard.getExpire() * 30;
            userWxMonthCardVo.setEndTime(DateUtils.addDays(date, days));
            userWxMonthCardVo.setOptionTime(record.getOptionTime());
            userWxMonthCardVo.setUsedCount(0);
            userWxMonthCardVo.setTotalCount(card.getCount());
            userWxMonthCardVo.setmUsedCount(0);
            userWxMonthCardVo.setmTotalCount(card.getCount());
            userWxMonthCardVo.setWxMonthCardId(record.getWxMonthCardId());
            userWxMonthCardVo.setAddressId(record.getAddressId());
            // 添加
            if (insert) {
                sqlMapClientTemplate.insert("wxMonthCard.insertUserMonthCard", userWxMonthCardVo);
            } else {
                sqlMapClientTemplate.update("wxMonthCard.updateUserMonthCard", userWxMonthCardVo);
            }
        } else { // 如果月卡没有过期
            // 未过期的不做处理，只保存支付状态，可以在后台手动退款
            log.info("微信小程序支付，月卡没有过期");
            doDiffCardPay(record, orderNo, (byte) 3,(byte)4);
            return resXml;
        }

        // 更新月卡充值记录为已完成
        doDiffCardPay(record, orderNo, (byte) 2,(byte)4);

        if (wxMonthCard.getCardType() != null) {
            if (wxMonthCard.getCardType().intValue() == 1) {
                appOrderService.calculateEarnings(orderNo.toString(), 2);
            }
        }
        log.info("微信小程序支付--成功!");
        return resXml;
    }

    public void sendMessage(String text, String phone) throws IOException {
        *//** 发送短信 **//*
        SmsApi.sendSms(Messages.getString("apikey"), text, phone);
    }*/
}
