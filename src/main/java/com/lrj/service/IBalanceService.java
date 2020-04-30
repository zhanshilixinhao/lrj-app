package com.lrj.service;

import com.lrj.VO.FormerResult;
import com.lrj.VO.UserMoneyInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lxh
 * @date 2020/4/2 9:20
 */
public interface IBalanceService {
    UserMoneyInfo findBalanceByUserId(Integer userId, FormerResult result, HttpServletRequest request);
}
