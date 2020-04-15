package com.lrj.service;

import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.OrderVo;
import com.lrj.VO.UserInfoVo;

import java.util.List;

/**
 * @author : cwj
 * @describe : 订单 服务
 * @date : 2020-4-3
 */
public interface IOrderService {

    /**
     * 创建订单
     */
    Integer createOrder(OrderVo orderVo);

    /**
     * 通过订单号和用户Id查询 唯一订单
     * @param orderId
     * @return
     */
    OrderVo findOrderByOrderId(String orderId);
}
