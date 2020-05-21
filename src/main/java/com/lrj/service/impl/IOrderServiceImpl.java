package com.lrj.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lrj.VO.*;
import com.lrj.constant.Constant;
import com.lrj.mapper.IMonthCardMapper;
import com.lrj.mapper.IOrderMapper;
import com.lrj.mapper.ReservationMapper;
import com.lrj.pojo.Consignee;
import com.lrj.pojo.MonthCard;
import com.lrj.pojo.Order;
import com.lrj.pojo.Reservation;
import com.lrj.service.*;
import com.lrj.util.DateUtils;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonValue;
import org.apache.ibatis.annotations.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private IShoppingService shoppingService;
    @Resource
    private ReservationMapper reservationMapper;
    @Resource
    private LaundryAppointmentService laundryAppointmentService;
    @Resource
    private IMonthCardMapper monthCardMapper;
    @Resource
    private IUserService userService;

    public Integer createOrder(OrderVo orderVo, HttpServletRequest request) {
        //不同订单走不同通道
        Integer orderType = orderVo.getOrderType();
        Integer insertNum = null;
        //封装预约参数
        Map<String, Object> reservationMap = new HashMap<String, Object>();
        //用户信息
        UserInfoVo userInfoVo = userService.findUserInfoByUserId(orderVo.getUserId());
        switch (orderType){
            //单项洗衣通道
            case 1:
                orderMapper.createOrder(orderVo);
                String takeTime = request.getParameter("takeTime");
                Integer takeConsigneeId =Integer.parseInt(request.getParameter("takeConsigneeId"));
                Integer sendConsigneeId = Integer.parseInt(request.getParameter("sendConsigneeId"));
                Integer isUrgent = 0;
                Order_washingVo washingOrder = new Order_washingVo();
                washingOrder.setCreateTime(DateUtils.getNowDateTime());
                washingOrder.setOrderNumber(orderVo.getOrderNumber());
                washingOrder.setIsLock(0);
                washingOrder.setIsUrgent(isUrgent);
                washingOrder.setTakeTime(takeTime);
                washingOrder.setSendConsigneeId(sendConsigneeId);
                washingOrder.setTakeConsigneeId(takeConsigneeId);
                /** 获取购物车商品列表 **/
                List<ShoppingVo> shoppingVoList = shoppingService.getShoppingDetails(orderVo.getUserId());
                JSONArray array = new JSONArray();
                for (ShoppingVo shoppingVo : shoppingVoList){
                    JSONObject shoppingJSON=new JSONObject();
                    shoppingJSON.put("itemId",shoppingVo.getItemId());
                    shoppingJSON.put("quantity",shoppingVo.getQuantity());
                    shoppingJSON.put("price",shoppingVo.getPrice());
                    shoppingJSON.put("itemName",shoppingVo.getItemName());
                    shoppingJSON.put("valueAddService", shoppingVo.getValueAddedServicesVos());
                    array.add(shoppingJSON);
                }
                washingOrder.setShoppingJSON(array.toString());
                //保存单项洗衣订单
               insertNum = orderMapper.createWashingOrder(washingOrder);
               //创建单项洗衣预约记录
                reservationMap = null;
                reservationMap.put("userId", orderVo.getUserId());
                reservationMap.put("userName",userInfoVo.getNickname());
                reservationMap.put("takeConsigneeId", takeConsigneeId);
                reservationMap.put("orderNumber", orderVo.getOrderNumber());
                laundryAppointmentService.createWashingAppoint(reservationMap);
               break;
            //月卡洗衣
            case 2:
                orderMapper.createOrder(orderVo);
                Integer monthCardId = Integer.parseInt(request.getParameter("monthCardId"));
                Order_monthCardVo monthCardOrder = new Order_monthCardVo();
                monthCardOrder.setActive(1);
                monthCardOrder.setCreateTime(DateUtils.getNowDateTime());
                monthCardOrder.setEndTime(DateUtils.getParamDateAfterNMonthDate(monthCardOrder.getCreateTime(),30));
                monthCardOrder.setOrderNumber(orderVo.getOrderNumber());
                //购买哪种月卡
                MonthCard monthCard = monthCardMapper.getMonthCardById(monthCardId);
                monthCardOrder.setUserMonthCardCount(monthCard.getCount());
                monthCardOrder.setMonthCard(monthCard);
                monthCardOrder.setUserId(orderVo.getUserId());
                //保存月卡订单
                insertNum = orderMapper.createMonthCardOrder(monthCardOrder);
                break;
            /*****************************单项家政服务*********************************/
            case 3:
                orderMapper.createOrder(orderVo);
                Order_houseServiceVo houseServiceOrder = new Order_houseServiceVo();
                houseServiceOrder.setOrderNumber(orderVo.getOrderNumber());
                houseServiceOrder.setIsLock(Constant.UNLOCK);
                houseServiceOrder.setTakeConsigneeId(Integer.parseInt(request.getParameter("takeConsigneeId")));
                houseServiceOrder.setHouseServiceId(Integer.parseInt(request.getParameter("houseServiceId")));
                //保存单项家政服务订单
                insertNum = orderMapper.createHouseServiceOrder(houseServiceOrder);
                //创建单项家政预约记录
                //封装预约参数
                reservationMap = null;
                reservationMap.put("userId", orderVo.getUserId());
                reservationMap.put("userName",userInfoVo.getNickname());
                reservationMap.put("takeConsigneeId", Integer.parseInt(request.getParameter("takeConsigneeId")));
                reservationMap.put("orderNumber", orderVo.getOrderNumber());
                laundryAppointmentService.createHouseServiceAppoint(reservationMap);
                break;
            /*****************************定制家政服务*********************************/
            case 4:
                orderMapper.createOrder(orderVo);
                Integer serviceCycle = Integer.parseInt(request.getParameter(" serviceCycle"));
                Integer baseServiceCount = Integer.parseInt(request.getParameter("baseServiceCount"));
                Integer workTime = Integer.parseInt(request.getParameter("workTime"));
                Integer houseArea = Integer.parseInt(request.getParameter("houseArea"));
                //定制家政订单
                Order_custom_houseServiceVo customHouseServiceOrder = new Order_custom_houseServiceVo();
                customHouseServiceOrder.setOrderNumber(orderVo.getOrderNumber());
                customHouseServiceOrder.setBaseServiceCount(baseServiceCount);
                customHouseServiceOrder.setWorkTime(workTime);
                customHouseServiceOrder.setHouseArea(houseArea);
                BigDecimal baseServicePrice = new BigDecimal(request.getParameter("baseServicePrice"));
                customHouseServiceOrder.setBaseServicePrice(baseServicePrice);
                Integer days = serviceCycle*30;
                customHouseServiceOrder.setOpenTime(DateUtils.getNowTime("yyyy-MM-DD"));
                customHouseServiceOrder.setEndTime(DateUtils.getParamDateAfterNDays("yyyy-MM-DD",days));
                customHouseServiceOrder.setIndividualServiceJson(request.getParameter("individualServiceJson"));
                BigDecimal individualServicePrice = new BigDecimal(request.getParameter("individualServicePrice"));
                customHouseServiceOrder.setIndividualServicePrice(individualServicePrice);
                //保存定制家政服务订单
                insertNum = orderMapper.createCustomHouseServiceOrder(customHouseServiceOrder);
                break;
        }
        return insertNum;
    }

    public OrderVo findOrderByOrderNumber(String orderNumber) {
        return orderMapper.getOrderByOrderNumber(orderNumber);
    }

    public void updateOrderPayStatus(String orderNumber) {
        orderMapper.updateOrderPayStatus(orderNumber);
    }

    public List<OrderVo> findOrderListByUserId(Integer userId) {
        return orderMapper.getOrderListByUserId(userId);
    }

    public Boolean lockOrderDetailIsLock(String orderNumber,Integer staffId) {
        //查找基础订单
        OrderVo orderVo = orderMapper.getOrderByOrderNumber(orderNumber);
        switch (orderVo.getOrderType()) {
            case 1:
                Order_washingVo washingOrder = orderMapper.getWashingOrderByOrderNumber(orderNumber);
                if (washingOrder.getIsLock() != 0) {
                    return false;
                } else {
                    //锁定洗衣订单 并绑定锁单人（供给后台管理系统查询使用,只有单项洗衣和家政有）
                    orderMapper.lockWashingOrder(orderNumber);
                    //锁定预约 并绑定锁单人
                    Integer lockNumber = reservationMapper.lockReservation(orderNumber, staffId);
                    if (lockNumber == 1) {
                        return true;
                    } else {
                        return false;
                    }
                }
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
        return null;
    }

    public void updateUserMonthCardCount(int monthCardCount, String orderNumber) {
        //封装参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("monthCardCount", monthCardCount);
        params.put("orderNumber", orderNumber);
        orderMapper.updateUserMonthCardCout(params);
    }

    public void updateUserHouseServiceBaseServiceCount(int houseServiceBaseServiceCount, String orderNumber) {
        //封装参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("houseServiceBaseServiceCount", houseServiceBaseServiceCount);
        params.put("orderNumber", orderNumber);
        orderMapper.updateUserHouseServiceBaseServiceCount(params);
    }



    /**
     * 添加订单备注
     * @param smsTemplateVo
     */
    public void addOrderRemark(SmsTemplateVo smsTemplateVo) {
        orderMapper.addOrderRemark(smsTemplateVo);
    }

    /**
     * 删除订单备注
     * @param orderNumber
     */
    public void deleteOrderRemark(String orderNumber) {
        orderMapper.deleteOrderRemark(orderNumber);
    }
}
