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
     * 通过订单号 唯一订单
     * @param orderNumber
     * @return
     */
    OrderVo findOrderByOrderNumber(String orderNumber);


    /**
     * 更改基础订单状态
     * @param orderNumber
     */
    void updateOrderPayStatus(String orderNumber);

    /**
     *通过userId 查询用户的订单
     * @param userId
     * @return
     */
    List<OrderVo> findOrderListByUserId(Integer userId);

    /**
     * 通过订单号 判断是哪种订单的状态（抢单）
     * @param orderNumber
     * @return
     */
    Boolean lockOrderDetailIsLock(String orderNumber,Integer staffId);

    /**
     * 通过订单号 更新月卡使用次数
     * @param monthCardCount
     * @param orderNumber
     */
    void updateUserMonthCardCount(int monthCardCount, String orderNumber);

    /**
     * 通过订单号 更新定制家政基础使用次数
     * @param houseServiceBaseServiceCount
     * @param orderNumber
     */
    void updateUserHouseServiceBaseServiceCount(int houseServiceBaseServiceCount, String orderNumber);
}
