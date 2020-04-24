package com.lrj.service.impl;

import com.lrj.VO.BuyCardOptionVo;
import com.lrj.VO.FormerResult;
import com.lrj.VO.OrderAppointVo;
import com.lrj.VO.UserMonthCardVo;
import com.lrj.mapper.*;
import com.lrj.pojo.*;
import com.lrj.service.LaundryAppointmentService;
import com.lrj.util.CommonUtil;
import com.lrj.util.DateUtils;

import com.lrj.util.RandomUtil;

import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

import java.util.List;

/**
 * @author Lxh
 * @date 2020/4/17 17:08
 */
@Service
public class LaundryAppointmentServiceImpl implements LaundryAppointmentService {
    @Resource
    private UserMonthCardMapper userMonthCardMapper;
    @Resource
    private CardOrderMapper cardOrderMapper;
    @Resource
    private CardCatMapper cardCatMapper;
    @Resource
    private ConsigneeMapper consigneeMapper;
    @Resource
    private ReservationMapper reservationMapper;
    @Override
    public FormerResult createAppoint(BuyCardOptionVo option) {
        FormerResult result = new FormerResult();
        UserMonthCardVo userMonthCardVo = userMonthCardMapper.selectUserMonthCard(option.getUserId());
        if (userMonthCardVo==null) {
            return CommonUtil.FAIL(result,"无可用月卡",null);
        }
        // 判断使用次数和期限
        if (userMonthCardVo.getMUsedCount().equals(userMonthCardVo.getMTotalCount()) ||
                userMonthCardVo.getEndTime().getTime() < System.currentTimeMillis()) {
            return CommonUtil.FAIL(result,"月卡次数已用完或已过期",null);
        }
        Order order = new Order();
        // 如果该用户购买了月卡, 判断需不需要传预约时间
        if (userMonthCardVo.getCardTimeType() == 1) {
            order.setTakeTime(DateUtils.formatDate(new Date(), "yyyy年MM月dd日 HH时mm分"));
        } else {
            // 如果是定时的预约
            return CommonUtil.FAIL(result,"该月卡不用预约!",null);
        }
        doAppoint(order,userMonthCardVo,option.getUserId(),option.getAddressId(),option.getIsShare());
        System.out.println(order.toString());
        return CommonUtil.SUCCESS(result,null,order);
    }

    @Override
    public Object changeAuto(BuyCardOptionVo option) {
        // 判断用户是否购买了月卡
        UserMonthCardVo userMonthCardVo = userMonthCardMapper.selectUserMonthCard(option.getUserId());
        if (userMonthCardVo==null) {
            return "无可用月卡";
        }
        // 如果存在，更新
        userMonthCardVo.setIsAuto(option.getIsAuto());
        userMonthCardMapper.updateByPrimaryKeySelective(userMonthCardVo);
        return null;
    }

    @Override
    public FormerResult getList(BuyCardOptionVo option) {
        List<OrderAppointVo> list = userMonthCardMapper.selectOrderList(option);
        return CommonUtil.SUCCESS(new FormerResult(),null,list);
    }

    private void doAppoint(Order order, UserMonthCard userMonthCard, Integer userId, Integer addressId, Byte isShare) {
        //用户
        long l = RandomUtil.generateOrderId();
        order.setOrderNumber(String.valueOf(l));
        order.setUserId(userId);
        order.setTotalPrice(new BigDecimal("0"));
        // 1 未完成 2 已完成
        order.setStatus(1);
        // 1 未支付 2 已支付
        order.setPayStatus(2);
        order.setTakeAddress(addressId);
        order.setReceiveAddress(addressId);
        order.setIsLock(1);
        order.setTraceStatus(1);
        order.setIsYearsService((byte) 5);
        order.setMonthCardId(userMonthCard.getWxMonthCardId());
        order.setIsShare(isShare).setOrderType(2).setCreateTime(DateUtils.formatDate(new Date(), "yyyy年MM月dd日 HH时mm分"));
        //保存订单
        cardOrderMapper.insertSelective(order);
        // 更新用户数据
        userMonthCard.setUsedCount(userMonthCard.getUsedCount() + 1);
        userMonthCard.setMUsedCount(userMonthCard.getMUsedCount() + 1);
        userMonthCard.setLastAppointTime(new Date());
        userMonthCardMapper.updateByPrimaryKeySelective(userMonthCard);
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderNumber", l);
        List<Order> orders = cardOrderMapper.selectByExample(example);
        // 保存预约订单
        CardCat cardCat = cardCatMapper.selectByPrimaryKey(userMonthCard.getWxMonthCardId());
        updateAMap(cardCat,orders);

    }

    private void updateAMap(CardCat cardCat, List<Order> orders) {
        Long id = null;
        for (Order order : orders) {
            id = order.getId();
        }

        Order order = cardOrderMapper.selectByPrimaryKey(id);
        Example example = new Example(Consignee.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("appConsigneeId",order.getTakeAddress());
        List<Consignee> consignees = consigneeMapper.selectByExample(example);
        String name = null;
        String address = null;
        for (Consignee consignee : consignees) {
            name = consignee.getName();
            address = consignee.getAddress();
        }

        if (address != null) {
            if (!address.contains("昆明")) {
                address = "昆明市" + address;
            }
            if (!address.contains("云南")) {
                address = "云南省" + address;
            }
            Reservation reservation = new Reservation();
            reservation.setAddress(address).setUserId(order.getUserId()).setOrderNumber(order.getOrderNumber()).setStatus((byte) 0).setOrderType(order.getOrderType()).setCreateTime(DateUtils.formatDate(new Date(), "yyyy年MM月dd日 HH时mm分"));
            reservationMapper.insertSelective(reservation);
            System.out.println(reservation.toString());
        }
    }
}
