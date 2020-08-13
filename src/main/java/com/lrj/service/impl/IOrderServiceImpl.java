package com.lrj.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lrj.VO.*;
import com.lrj.constant.Constant;
import com.lrj.mapper.IItemMapper;
import com.lrj.mapper.IMonthCardMapper;
import com.lrj.mapper.IOrderMapper;
import com.lrj.mapper.ReservationMapper;
import com.lrj.pojo.MerchantOrder;
import com.lrj.pojo.MonthCard;
import com.lrj.pojo.Reservation;
import com.lrj.service.*;
import com.lrj.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.SignedObject;
import java.util.*;

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
    @Resource
    private IItemMapper itemMapper;

    public Integer createOrder(OrderVo orderVo, HttpServletRequest request, Map<String,Object> typeParams) {
        //不同订单走不同通道
        Integer orderType = orderVo.getOrderType();
        Integer insertNum = null;
        //封装预约参数
        Map<String, Object> reservationMap = new HashMap<String, Object>();
        //用户信息
        UserInfoVo userInfoVo = userService.findUserInfoByUserId(orderVo.getUserId());
        orderVo.setUserPhone(userInfoVo.getUserPhone());
        switch (orderType){
            //单项洗衣通道
            case 1:
                orderVo.setLevelPrice((BigDecimal) typeParams.get("levelPrice"));
                orderMapper.createOrder(orderVo);
                Order_washingVo washingOrder = new Order_washingVo();
                washingOrder.setCreateTime(DateUtils.getNowDateTime());
                washingOrder.setOrderNumber(orderVo.getOrderNumber());
                washingOrder.setIsLock(0);
                washingOrder.setUrgentPrice((BigDecimal) typeParams.get("urgentPrice"));
                washingOrder.setTakeTime((String) typeParams.get("takeTime"));
                washingOrder.setSendConsigneeId((Integer) typeParams.get("sendConsigneeId"));
                washingOrder.setTakeConsigneeId((Integer) typeParams.get("takeConsigneeId"));
                washingOrder.setServicePrice((BigDecimal) typeParams.get("servicePrice"));
                washingOrder.setLevelPrice((BigDecimal) typeParams.get("levelPrice"));

                if(typeParams.get("detailJson") !=null){
                    washingOrder.setShoppingJson((String) typeParams.get("detailJson"));
                }else {
                    /** 获取购物车商品列表 **/
                    List<ShoppingVo> shoppingVoList = shoppingService.getShoppingDetails(orderVo.getUserId());
                    //商品信息 方式一
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
                    washingOrder.setShoppingJson(array.toJSONString());
                    //清空购物车
                    shoppingService.emptyShopCart(orderVo.getUserId());
                }
                //保存单项洗衣订单
               insertNum = orderMapper.createWashingOrder(washingOrder);
               //创建单项洗衣预约记录
                reservationMap.put("userId", orderVo.getUserId());
                reservationMap.put("userName",userInfoVo.getNickname());
                reservationMap.put("takeConsigneeId",typeParams.get("takeConsigneeId"));
                reservationMap.put("orderNumber", orderVo.getOrderNumber());
                laundryAppointmentService.createWashingAppoint(reservationMap);
               break;
            //月卡洗衣
            case 2:
                orderMapper.createOrder(orderVo);
                Order_monthCardVo monthCardOrder = new Order_monthCardVo();
                monthCardOrder.setActive(1);
                monthCardOrder.setCreateTime(DateUtils.getNowDateTime());
                monthCardOrder.setEndTime(DateUtils.getParamDateAfterNMonthDate(monthCardOrder.getCreateTime(),1));
                monthCardOrder.setOrderNumber(orderVo.getOrderNumber());
                //购买哪种月卡
                MonthCard monthCard = monthCardMapper.getMonthCardById((Integer) typeParams.get("monthCardId"));
                monthCardOrder.setUserMonthCardCount(monthCard.getCount());
                monthCardOrder.setMonthCardId(monthCard.getCardId());
                monthCardOrder.setUserId(orderVo.getUserId());
                //月卡具体商品信息
                List<MonthCardWashingCountVo> monthCardDetailList = monthCardMapper.getMonthCardWashingCountList((Integer) typeParams.get("monthCardId"));
                //拼接月卡的其他信息
                for(MonthCardWashingCountVo monthCardWashingCountVo: monthCardDetailList){
                    //单位
                    AppItemVo itemInfo = itemMapper.getItemInfoByItemId(monthCardWashingCountVo.getItemId());
                    monthCardWashingCountVo.setItemUnit(itemInfo.getItemUnit());
                    //名字
                    ItemCategoryVo itemCategoryVo = itemMapper.getItemCategoryInfoByCategoryId(itemInfo.getItemCategoryId());
                    monthCardWashingCountVo.setItemCategoryName(itemCategoryVo.getCategoryName());
                }
                String json = JSONArray.toJSONString(monthCardDetailList);
                monthCardOrder.setUserMonthCardItemJson(json);
                //保存月卡订单
                insertNum = orderMapper.createMonthCardOrder(monthCardOrder);
                break;
            /*****************************单项家政服务*********************************/
            case 3:
                orderMapper.createOrder(orderVo);
                //拼接家政信息
                Order_houseServiceVo houseServiceOrder = new Order_houseServiceVo();
                houseServiceOrder.setOrderNumber(orderVo.getOrderNumber());
                houseServiceOrder.setIsLock(Constant.UNLOCK);
                houseServiceOrder.setActive(1);
                houseServiceOrder.setTakeConsigneeId((Integer) typeParams.get("takeConsigneeId"));
                houseServiceOrder.setCreateTime(DateUtils.getNowDateTime());
                houseServiceOrder.setHouseServiceJson((String) typeParams.get("houseServiceJson"));
                //保存单项家政服务订单
                insertNum = orderMapper.createHouseServiceOrder(houseServiceOrder);
                //创建单项家政预约记录
                //封装预约参数
                reservationMap.put("userId",orderVo.getUserId());
                reservationMap.put("userName",userInfoVo.getNickname());
                reservationMap.put("takeConsigneeId",typeParams.get("takeConsigneeId"));
                reservationMap.put("orderNumber", orderVo.getOrderNumber());
                laundryAppointmentService.createHouseServiceAppoint(reservationMap);
                break;
            /*****************************定制家政服务*********************************/
            case 4:
                orderMapper.createOrder(orderVo);
                //定制家政订单
                Order_custom_houseServiceVo customHouseServiceOrder = new Order_custom_houseServiceVo();
                customHouseServiceOrder.setOrderNumber(orderVo.getOrderNumber());
                customHouseServiceOrder.setBaseServiceCount((Integer) typeParams.get("baseServiceCount"));
                customHouseServiceOrder.setWorkTime((Integer) typeParams.get("workTime"));
                customHouseServiceOrder.setHouseArea((String) typeParams.get("houseArea"));
                customHouseServiceOrder.setServiceCycle((Integer) typeParams.get("serviceCycle"));
                customHouseServiceOrder.setBaseServicePrice((BigDecimal) typeParams.get("baseServicePrice"));
                customHouseServiceOrder.setOpenTime(DateUtils.getNowTime("yyyy-MM-DD HH:mm:ss"));
                customHouseServiceOrder.setEndTime(DateUtils.getParamDateAfterNMonthDate(customHouseServiceOrder.getOpenTime(),(int)typeParams.get("serviceCycle")));
                customHouseServiceOrder.setIndividualServiceJson((String) typeParams.get("individualServiceJson"));
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

    @Override
    public List<OrderVo> findOrderListByUserIdAndStatus(Integer userId, Integer status) {
        Map<String, Integer> params = new HashMap<>();
        params.put("userId", userId);
        params.put("status", status);
        return orderMapper.getOrderListByUserIdAndStatus(params);
    }

    public Boolean lockOrderDetailIsLock(String reservationId,Integer staffId) {
        //查找服务单
        Reservation reservation = reservationMapper.getReservationByReservationId(Integer.parseInt(reservationId));
        if (reservation.getGrabOrderIdTake() != null || reservation.getGrabOrderIdSend() !=null) {
            return false;
        } else {
            //锁定服务单 并绑定锁单人
            Map<String, Object> params = new HashMap<>();
            params.put("reservationId", reservationId);
            params.put("staffId", staffId);
            Integer lockNumber = reservationMapper.lockReservation(params);
            if (lockNumber == 1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void updateUserMonthCardCount(int monthCardCount, String orderNumber) {
        //封装参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("monthCardCount", monthCardCount);
        params.put("orderNumber", orderNumber);
        orderMapper.updateUserMonthCardCout(params);
    }

    @Override
    public void updateUserMonthCardItemJson(String washingDetailJSONArrayAll, String orderNumber) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userMonthCardItemJson", washingDetailJSONArrayAll);
        params.put("orderNumber", orderNumber);
        orderMapper.updateUserMonthCardItemJson(params);
    }

    public void updateUserHouseServiceBaseServiceCount(int houseServiceBaseServiceCount, String orderNumber) {
        //封装参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("houseServiceBaseServiceCount", houseServiceBaseServiceCount);
        params.put("orderNumber", orderNumber);
        orderMapper.updateUserHouseServiceBaseServiceCount(params);
    }

    @Override
    public void updateIndividualServiceJson(String IndividualServiceJson, String orderNumber) {
        Map<String, String> params = new HashMap<>();
        params.put("individualServiceJson", IndividualServiceJson);
        params.put("orderNumber", orderNumber);
        orderMapper.updateIndividualServiceJson(params);
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

    /**
     * 更新订单分享 状态
     * @param isShare
     */
    @Override
    public void updateOrderIsShare(Integer isShare,String shareOrderNumber) {
        Map<String, Object> params = new HashMap<>();
        params.put("isShare", isShare);
        params.put("shareOrderNumber", shareOrderNumber);
        orderMapper.updateOrderIsShare(params);
    }
}
