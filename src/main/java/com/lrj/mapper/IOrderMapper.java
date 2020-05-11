package com.lrj.mapper;

import com.lrj.VO.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : cwj
 * @describe : 订单 有关接口
 * @date : 2020-4-2
 */
@Repository
public interface IOrderMapper {
    /**
     * 创建基础订单
     */
    Integer createOrder(OrderVo orderVo);
    /**
     * 创建 单项洗衣订单
     * @param washingOrder
     */
    Integer createWashingOrder(Order_washingVo washingOrder);

    /**
     * 创建单项家政服务订单
     */
    Integer createHouseServiceOrder(Order_houseServiceVo houseServiceVo);
    /**
     * 创建 定制家政服务订单
     */
    Integer createCustomHouseServiceOrder(Order_custom_houseServiceVo customHouseServiceVo);

    /**
     * 通过订单Id查询订单数据
     * @return
     */
    OrderVo getOrderByOrderNumber(String appOrderId);

    /**
     * 更改基础订单 支付状态
     * @param orderNumber
     */
    void updateOrderPayStatus(String orderNumber);

    /**
     * 查询用户的所有订单
     * @param userId
     * @return
     */
    List<OrderVo> getOrderListByUserId(Integer userId);

    /**
     * 查询单项洗衣订单
     */
    Order_washingVo getWashingOrderByOrderNumber(String orderNumber);

    /**
     * 锁定单项洗衣订单
     * @param orderNumber
     */
    Integer lockWashingOrder(String orderNumber);
}
