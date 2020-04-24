package com.lrj.service.impl;


import com.lrj.VO.OrderVo;
import com.lrj.VO.Order_houseServiceVo;
import com.lrj.common.Constant;
import com.lrj.mapper.IOrderMapper;
import com.lrj.service.IOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author : cwj
 * @describe : 订单 服务实现
 * @date : 2020-4-1
 */
@Service
@Transactional
public class IOrderServiceImpl implements IOrderService {

    @Resource
    private IOrderMapper orderMapper;

    @Override
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

    @Override
    public OrderVo findOrderByOrderId(String orderId) {
        Integer orderIdInteger = Integer.getInteger(orderId);
        return orderMapper.getOrderByOrderId(orderIdInteger);
    }
}
