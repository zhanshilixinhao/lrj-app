package com.lrj.service;

import com.lrj.VO.AliPayVo;
import com.lrj.pojo.BalanceRecord;
import com.lrj.pojo.PayOperation;

import java.io.IOException;
import java.util.Map;

public interface IPayService {

    /**
     * 微信支付回调
     *
     * @param xml 微信支付参数
     * @return
     */
    String wxminiPayNotify(String xml) throws Exception;

    /**
     * app月卡购买微信支付回调
     * @param xml
     * @return
     * @throws IOException
     */
    String appWXPayNotify(String xml) throws Exception;

    /**
     *   流水记录
     * @param payOperation
     */
    void payFlowRecord(PayOperation payOperation);

    /**
     * 提现申请
     * @param payOperation
     */
    Integer userWithdrawApply(PayOperation payOperation);

    /**
     * 增加用户 资金流水记录
     * @param balanceRecord1
     */
    void addUserBalanceRecord(BalanceRecord balanceRecord1);

    /**
     * 更改用户 资金流水状态
     * @param rechargeOrderNumber
     */
    void updateUserBalanceRecord(String rechargeOrderNumber);
}
