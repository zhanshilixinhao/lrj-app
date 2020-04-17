package com.lrj.service.impl;

import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.OrderVo;
import com.lrj.VO.Order_houseServiceVo;
import com.lrj.VO.UserInfoVo;
import com.lrj.constant.Constant;
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
        //不同订单走不同通道
        Integer orderType = orderVo.getOrderType();
        switch (orderType){
            //单项洗衣通道
            case 1:
            //月卡洗衣
            case 2:
            //单项家政服务
            case 3:
                Order_houseServiceVo houseServiceOrder = new Order_houseServiceVo();
                houseServiceOrder.setIsLock(Constant.UNLOCK);
            //定制家政服务
            case 4:
        }
        return orderMapper.createOrder(orderVo);
    }

    public OrderVo findOrderByOrderId(String orderId) {
        Integer orderIdInteger = Integer.getInteger(orderId);
        return orderMapper.getOrderByOrderId(orderIdInteger);
    }
}
