package com.lrj.service.impl;

import com.lrj.VO.FormerResult;
import com.lrj.VO.UserMoneyInfo;
import com.lrj.mapper.BalanceMapper;
import com.lrj.mapper.GivenBalanceMapper;
import com.lrj.mapper.UserCouponMapper;
import com.lrj.pojo.Balance;
import com.lrj.pojo.GivenBalance;
import com.lrj.service.IBalanceService;
import com.lrj.util.CommonUtil;
import com.lrj.util.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Override
    public FormerResult findBalanceByUserId(Integer userId, FormerResult result, HttpServletRequest request) {

        Balance balance = balanceMapper.selectByPrimaryKey(userId);
        BigDecimal reBalance = balance.getBalance();
        Integer nums = userCouponMapper.selectQuantityByUserId(userId);
        List<GivenBalance> all = givenBalanceMapper.selectAllByUserId(userId);
        List<GivenBalance> list = new ArrayList<>();
        Integer isDaixi = null;
        if (request.getParameter("isDaixi") != null) {
            isDaixi = Integer.parseInt(request.getParameter("isDaixi"));
        }
        if (isDaixi != null) {
            if (isDaixi == 1 && all.size() > 0) {
                for (GivenBalance givenBalance: all) {
                    if (givenBalance.getStatus() == null) {
                        list.add(givenBalance);
                    } else if (givenBalance.getStatus().intValue() == 3 || givenBalance.getStatus().intValue() == 1) {
                        list.add(givenBalance);
                    }
                }
            } else if(isDaixi == 2 && all.size() > 0 ) {
                for (GivenBalance givenBalance: all) {
                    if (givenBalance.getStatus() == null) {
                        list.add(givenBalance);
                    } else if (givenBalance.getStatus().intValue() == 4 || givenBalance.getStatus().intValue() == 1) {
                        list.add(givenBalance);
                    }
                }
            } else {
                list = all;
            }
        }
        else {
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
        /** 去实体对象中的null值 **/
        CommonUtil.beanToRemoveNull(umf);
        return CommonUtil.SUCCESS(result, "获取信息成功", umf);
    }
}
