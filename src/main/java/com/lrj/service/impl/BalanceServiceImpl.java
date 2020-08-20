package com.lrj.service.impl;

import com.lrj.mapper.BalanceMapper;
import com.lrj.mapper.GivenBalanceMapper;
import com.lrj.mapper.UserCouponMapper;
import com.lrj.pojo.BalanceRecord;
import com.lrj.service.IBalanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Lxh
 * @date 2020/4/2 9:21
 */
@Service
public class BalanceServiceImpl implements IBalanceService {
    @Resource
    private BalanceMapper balanceMapper;
    @Resource
    private GivenBalanceMapper givenBalanceMapper;
    @Resource
    private UserCouponMapper userCouponMapper;

   /* public UserMoneyInfo findBalanceByUserId(Integer userId, FormerResult result, HttpServletRequest request) {

        Balance balance = balanceMapper.selectByPrimaryKey(userId);
        BigDecimal reBalance = balance.getBalance();
        Integer nums = userCouponMapper.selectQuantityByUserId(userId);
        List<GivenBalance> all = givenBalanceMapper.selectAllByUserId(userId);
        List<GivenBalance> list = new ArrayList<GivenBalance>();
        Integer isDaixi = null;
        if (request.getParameter("isDaixi") != null) {
            isDaixi = Integer.parseInt(request.getParameter("isDaixi"));
        }
        if (isDaixi != null) {
            if (isDaixi == 1 && all.size() > 0) {
                for (GivenBalance givenBalance : all) {
                    if (givenBalance.getStatus() == null) {
                        list.add(givenBalance);
                    } else if (givenBalance.getStatus().intValue() == 3 || givenBalance.getStatus().intValue() == 1) {
                        list.add(givenBalance);
                    }
                }
            } else if (isDaixi == 2 && all.size() > 0) {
                for (GivenBalance givenBalance : all) {
                    if (givenBalance.getStatus() == null) {
                        list.add(givenBalance);
                    } else if (givenBalance.getStatus().intValue() == 4 || givenBalance.getStatus().intValue() == 1) {
                        list.add(givenBalance);
                    }
                }
            } else {
                list = all;
            }
        } else {
            list = all;
        }
        UserMoneyInfo umf = new UserMoneyInfo();
        umf.setBalance(reBalance);
        umf.setCouponNum(nums);
        umf.setGivenBalance(list);
        if (!list.isEmpty()) {
            String time = list.get(0).getExpirationTime();
            time = time.substring(0, time.indexOf("."));
            int days = 0;
            try {
                days = DateUtils.daysBetween(new Date(), DateUtils.formatStringToDate(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            umf.setDays(days);
        }
        *//** 去实体对象中的null值 **//*
        CommonUtil.beanToRemoveNull(umf);
        return umf;
    }*/

    public void updateUserBalance(double balanceMoney,Integer userId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("balanceMoney", balanceMoney);
        params.put("userId", userId);
        balanceMapper.updateUserBalance(params);
    }

    @Override
    public BalanceRecord findBalanceByRechargeOrderNumber(String rechargeOrderNumber) {
        return balanceMapper.findBalanceByRechargeOrderNumber(rechargeOrderNumber);
    }
}
