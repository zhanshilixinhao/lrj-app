package com.lrj.service.impl;

import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.OrderVo;
import com.lrj.VO.UserInfoVo;
import com.lrj.mapper.IOrderMapper;
import com.lrj.mapper.IUserMapper;
import com.lrj.service.IOrderService;
import com.lrj.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : cwj
 * @describe : 订单 服务实现
 * @date : 2020-4-1
 */
@Service
@Transactional
public class IOrderServiceImpl implements IOrderService{

    @Resource
    private IOrderMapper orderMapper;

    public Integer createOrder(OrderVo orderVo) {
        return orderMapper.createOrder(orderVo);
    }

    public OrderVo findOrderByOrderId(String orderId) {
        Integer orderIdInteger = Integer.getInteger(orderId);
        return orderMapper.getOrderByOrderId(orderIdInteger);
    }
}
