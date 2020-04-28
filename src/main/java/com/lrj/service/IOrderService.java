package com.lrj.service;

import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.OrderVo;
import com.lrj.VO.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 订单 服务
 * @date : 2020-4-3
 */
public interface IOrderService {

    /**
     * 创建订单
     */
    Integer createOrder(OrderVo orderVo, HttpServletRequest request);

    /**
     * 通过订单号和用户Id查询 唯一订单
     * @param orderNumber
     * @return
     */
    OrderVo findOrderByOrderNumber(String orderNumber);


    /**
     * 更改基础订单状态
     * @param orderNumber
     */
    void updateOrderPayStatus(String orderNumber);
}
