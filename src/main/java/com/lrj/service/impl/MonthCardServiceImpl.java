package com.lrj.service.impl;

import com.lrj.VO.BuyCardOptionVo;
import com.lrj.VO.FormerResult;
import com.lrj.VO.UserMonthCardVo;
import com.lrj.mapper.*;

import com.lrj.pojo.MonthCard;

import com.lrj.pojo.Order;
import com.lrj.pojo.User;
import com.lrj.pojo.UserMonthCardOrder;
import com.lrj.service.IMonthCardService;
import com.lrj.util.CommonUtil;

import com.lrj.util.RandomUtil;
import org.apache.commons.lang.StringUtils;


import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Lxh
 * @date 2020/4/16 11:50
 */
@Service
public class MonthCardServiceImpl implements IMonthCardService {
    @Resource
    private IMonthCardMapper IMonthCardMapper;
    @Resource
    private UserMapper userMapper;


    public List<MonthCard> getMonthCardList() {
        return IMonthCardMapper.getMonthCardList();
    }

    /*public FormerResult selectUserMonthCard(Integer userId) {
        UserMonthCardVo userMonthCardVo = IUserMonthCardMapper.selectUserMonthCard(userId);
        if (userMonthCardVo == null) {
            userMonthCardVo = new UserMonthCardVo();
            userMonthCardVo.setStatus((byte) -1);
            return CommonUtil.SUCCESS(new FormerResult(),"",userMonthCardVo);
        } else {
            if (userMonthCardVo.getEndTime() != null && userMonthCardVo.getEndTime().getTime() < System.currentTimeMillis()) {
                userMonthCardVo.setStatus((byte) 2);
                userMonthCardVo.setRemainCount(0);
                return CommonUtil.SUCCESS(new FormerResult(),"",userMonthCardVo);
            }
            if (userMonthCardVo.getUsedCount().intValue() == userMonthCardVo.getTotalCount().intValue()) {
                userMonthCardVo.setStatus((byte) -1);
            }
        }
        userMonthCardVo.setRemainCount(userMonthCardVo.getTotalCount() - userMonthCardVo.getUsedCount());

        return CommonUtil.SUCCESS(new FormerResult(),"",userMonthCardVo);
    }

    @Override
    public FormerResult buyCard(BuyCardOptionVo option) {
        FormerResult result = new FormerResult();
        User user = userMapper.selectByPrimaryKey(option.getUserId());
        if (user==null) {
            return CommonUtil.FAIL(result,"该账户不存在",null);
        }
        // 取出用户当前的月卡
        UserMonthCardVo userMonthCardVo = IUserMonthCardMapper.selectUserMonthCard(user.getAppUserId());
        if (option.getWxMonthCardId()==null) {
            return CommonUtil.FAIL(result,"请选择月卡种类",null);
        }
        // 取出用户要购买的月卡
        MonthCard monthCard = IMonthCardMapper.selectByPrimaryKey(option.getWxMonthCardId());
        // 如果用户存在月卡
        if (userMonthCardVo!=null) {
            // 判断月卡是否已过期,
            if (userMonthCardVo.getEndTime() != null && userMonthCardVo.getEndTime().getTime()
                    > System.currentTimeMillis()) {
                // 如果没有过期，只能买和之前相同的月卡
                // 判断已有月卡和将要购买的是不是同一个月卡
                if (!userMonthCardVo.getMUsedCount().equals(userMonthCardVo.getMTotalCount())) {
                    return CommonUtil.FAIL(result,"您已购买了一张月卡,请勿重复购买!",null);
                }
            }
// 如果月卡已过期，如果已过期可以买任意的月卡
        }
        UserMonthCardOrder record = new UserMonthCardOrder();
        Order order = setUpOrder(new Order());
        order.setUserId(option.getUserId()).setStatus(1);
        // 如果需要选择时间
        if (monthCard.getTimeType() == 2) {
            // 必须选择上门时间和收货地址
            if (StringUtils.isBlank(option.getOptionTime()) ||
                    option.getAddressId() == null) {
                return CommonUtil.FAIL(result,"请选择上门时间!",null);
            }
            // 上门时间
            try {
                record.setOptionTime(getWeekTime(option.getOptionTime()));
            } catch (Exception e) {
                return CommonUtil.FAIL(result,"短信验证码错误!",null);
            }
            record.setAddressId(option.getAddressId());
        }
        // 用户id
        // 判断是否分享了
        BigDecimal price = monthCard.getPrice();
        if (option.getIsShare() != null && option.getIsShare() == 1) {
            price = price.multiply(new BigDecimal(String.valueOf(monthCard.getShareDicount())))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        //保存订单id
        record.setOrderId(order.getId());
        // 月卡价格
        order.setTotalPrice(price);
        // 月卡id
        record.setWxMonthCardId(monthCard.getId());
        // 保存购买记录
        System.out.println(record.toString());
        System.out.println(order.toString());
        int insert = userMonthCardOrderMapper.insert(record);
        int i = cardOrderMapper.updateByPrimaryKeySelective(order);
        System.out.println(insert+"________"+i);
        return null;
    }

    private Order setUpOrder(Order order) {
        long l = RandomUtil.generateOrderId();
        String s = Long.toString(l);
        order.setOrderType(2).setOrderNumber(s).setCreateTime(com.lrj.util.DateUtils.formatDate(new Date()));
        cardOrderMapper.insert(order);
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderNumber",s);
        List<Order> orders = cardOrderMapper.selectByExample(example);
        for (Order order1 : orders) {
            return order1;
        }
        return null;
    }

    private static String getWeekTime(String time) {
        String[] split = time.split("@");
        int week = Integer.parseInt(split[0]);
        int currentWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        if (currentWeek == 0) {
            currentWeek = 7;
        }

        int day = week - currentWeek;
        Date date = DateUtils.addDays(new Date(), day < 0 ? day + 7 : day);
        String dateStr = com.lrj.util.DateUtils.formatDate(date, "yyyy-MM-dd");
        date = com.lrj.util.DateUtils.formatStringToDate(String.format("%s 08:00:00", dateStr));
        return com.lrj.util.DateUtils.formatDate(date);
    }*/
}
