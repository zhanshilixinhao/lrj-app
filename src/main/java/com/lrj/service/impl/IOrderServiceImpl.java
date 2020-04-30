package com.lrj.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lrj.VO.*;
import com.lrj.constant.Constant;
import com.lrj.mapper.IOrderMapper;
import com.lrj.service.IOrderService;
import com.lrj.service.IShoppingService;
import com.lrj.service.IUserService;
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

    @Override
    public Integer createOrder(OrderVo orderVo, HttpServletRequest request) {
        //不同订单走不同通道
        Integer orderType = orderVo.getOrderType();
        Integer insertNum = null;
        switch (orderType){
            //单项洗衣通道
            case 1:
                orderMapper.createOrder(orderVo);
                String takeTime = request.getParameter("takeTime");
                Integer takeConsigneeId = 1;//Integer.parseInt(request.getParameter("takeConsigneeId"));
                Integer sendConsigneeId = 1;//Integer.parseInt(request.getParameter("sendConsigneeId"));

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
                    array.add(shoppingJSON);
                }
                washingOrder.setShoppingJSON(array.toString());
                //保存单项洗衣订单
               insertNum = orderMapper.createWashingOrder(washingOrder);
               break;
            //月卡洗衣
            case 2:
            /*****************************单项家政服务*********************************/
            case 3:
                orderMapper.createOrder(orderVo);
                Order_houseServiceVo houseServiceOrder = new Order_houseServiceVo();
                houseServiceOrder.setOrderNumber(orderVo.getOrderNumber());
                houseServiceOrder.setIsLock(Constant.UNLOCK);
                houseServiceOrder.setItemId(Integer.parseInt(request.getParameter("itemId")));
                //保存单项家政服务订单
                insertNum = orderMapper.createHouseServiceOrder(houseServiceOrder);
                break;
            /*****************************定制家政服务*********************************/
            case 4:
                orderMapper.createOrder(orderVo);
                Integer serviceCycle = Integer.parseInt(request.getParameter(" serviceCycle"));
                Integer baseService = Integer.parseInt(request.getParameter("baseService"));
                //定制几个月就有几个子订单
                for(int i=0;i<serviceCycle;i++){
                    Order_custom_houseServiceVo customHouseServiceOrder = new Order_custom_houseServiceVo();
                    customHouseServiceOrder.setOrderNumber(orderVo.getOrderNumber());
                    BigDecimal baseServicePrice = new BigDecimal(request.getParameter("baseServicePrice"));
                    customHouseServiceOrder.setBaseServicePrice(baseServicePrice);
                    Integer days = (i+1)*30;
                    customHouseServiceOrder.setOpenTime(DateUtils.getNowTime("yyyy-MM-DD"));
                    customHouseServiceOrder.setEndTime(DateUtils.getParamDateAfterNDays("yyyy-MM-DD",days));
                    customHouseServiceOrder.setIndividualServiceJson(request.getParameter("individualServiceJson"));
                    BigDecimal individualServicePrice = new BigDecimal(request.getParameter("individualServicePrice"));
                    customHouseServiceOrder.setIndividualServicePrice(individualServicePrice);
                    //保存定制家政服务订单
                    insertNum = orderMapper.createCustomHouseServiceOrder(customHouseServiceOrder);
                    break;
                }
        }
        return insertNum;
    }

    @Override
    public OrderVo findOrderByOrderNumber(String orderNumber) {
        return orderMapper.getOrderByOrderNumber(orderNumber);
    }

    public void updateOrderPayStatus(String orderNumber) {
        orderMapper.updateOrderPayStatus(orderNumber);
    }
}
