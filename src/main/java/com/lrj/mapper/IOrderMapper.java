package com.lrj.mapper;

import com.lrj.VO.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
     *创建 月卡订单
     * @param monthCardOrder
     * @return
     */
    Integer createMonthCardOrder(Order_monthCardVo monthCardOrder);

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
     * 查询用户的所有订单（userId,status）
     * @param params
     * @return
     */
    List<OrderVo> getOrderListByUserIdAndStatus(Map<String,Integer> params);

    /**
     * 查询单项洗衣订单
     */
    Order_washingVo getWashingOrderByOrderNumber(String orderNumber);
    /**
     * 查询用户可用月卡订单(订单号)
     * @param orderNumber
     * @return
     */
    Order_monthCardVo getMonthCatdOrder(String orderNumber);
    /**
     * 查询用户可用月卡订单（userId）
     * @param userId
     * @return
     */
    Order_monthCardVo getMonthCatdOrderByUserId(Integer userId);
    /**
     * 查询用户月卡购买月卡记录
     */
    List<Order_monthCardVo> getMonthCatdOrderListByUserId(Integer userId);

    /**
     * 查询单项家政服务订单(orderNumber)
     * @param orderNumber
     * @return
     */
    Order_houseServiceVo getHouseServiceByOrderNumber(String orderNumber);
    /**
     * 查询单项家政服务订单(userId)
     * @param userId
     * @return
     */
    Order_custom_houseServiceVo getCustomHouseServiceOrderByUserId(Integer userId);
    /**
     * 查询定制服务购买记录
     */
    List<Order_custom_houseServiceVo> getCustomHouseServiceOrderListByUserId(Integer userId);
    /**
     * 查询定制家政服务订单
     * @param orderNumber
     * @return
     */
    Order_custom_houseServiceVo getCustomHouseServiceByOrderNumber(String orderNumber);

    /**
     * 锁定单项洗衣订单
     * @param orderNumber
     */
    Integer lockWashingOrder(String orderNumber);

    /**
     * 修改月卡可以使用次数
     * @param params
     */
    void updateUserMonthCardCout(Map<String,Object> params);

    /**
     * 更新月卡使用剩余商品具体信息
     * @param userMonthCardItemJson
     * @param orderNumber
     */
    void updateUserMonthCardItemJson(String userMonthCardItemJson,String orderNumber);

    /**
     * 修改月卡为不可用
     */
    void updateUserMonthCardActive(String orderNumber);

    /**
     * 修改 定制家政为不可用
     * @param orderNumber
     */
    void updateUserHouseServiceActive(String orderNumber);

    /**
     * 修改 定制家政可使用次数
     * @param params
     */
    void updateUserHouseServiceBaseServiceCount(Map<String, Object> params);

    /**
     * 更新定制家政使用剩余具体信息
     * @param individualServiceJson
     * @param orderNumber
     */
    void updateIndividualServiceJson(String individualServiceJson, String orderNumber);

    /**
     * 添加订单备注
     * @param smsTemplateVo
     */
    void addOrderRemark(SmsTemplateVo smsTemplateVo);

    /**
     * 删除订单 备注
     * @param orderNumber
     */
    void deleteOrderRemark(String orderNumber);
}
