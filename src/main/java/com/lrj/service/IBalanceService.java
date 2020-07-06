package com.lrj.service;

import com.lrj.VO.FormerResult;
import com.lrj.VO.UserMoneyInfo;
import com.lrj.pojo.BalanceRecord;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lxh
 * @date 2020/4/2 9:20
 */
public interface IBalanceService {

    /**
     * 修改用户余额
     * @param balanceMoney
     */
    void updateUserBalance(double balanceMoney,Integer userId);


    /**
     * 通过支付订单号  查询用户该笔充值交易
     * @param rechargeOrderNumber
     * @return
     */
    BalanceRecord findBalanceByRechargeOrderNumber(String rechargeOrderNumber);
}
