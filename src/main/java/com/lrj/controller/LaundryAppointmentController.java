package com.lrj.controller;
import com.lrj.VO.AppItemVo;
import com.lrj.VO.FormerResult;
import com.lrj.VO.Order_custom_houseServiceVo;
import com.lrj.VO.Order_monthCardVo;
import com.lrj.mapper.IItemJSONMapper;
import com.lrj.mapper.IItemMapper;
import com.lrj.mapper.IOrderMapper;
import com.lrj.pojo.ItemJSON;
import com.lrj.service.IOrderService;
import com.lrj.service.LaundryAppointmentService;

import com.lrj.util.BigDecimalUtil;
import com.lrj.util.DateUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lxh
 * @date 2020/4/17 16:43
 */
@RestController
@Transactional
public class LaundryAppointmentController {
    @Resource
    private LaundryAppointmentService laundryAppointmentService;
    @Resource
    private IOrderMapper orderMapper;
    @Resource
    private IOrderService orderService;
    @Resource
    private IItemJSONMapper itemJSONMapper;
    @Resource
    private IItemMapper itemMapper;

    /**
     * 预约洗衣
     * @param request
     * @return
     */
        @RequestMapping(value = "/createWashingAppoint",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult createWashingAppoint(HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        String userName = request.getParameter("userName");
        Integer takeConsigneeId = Integer.parseInt(request.getParameter("takeConsigneeId"));
        String reservationJson = request.getParameter("reservationJson");
        //效验必须参数
        if (userId == null || userName == null || takeConsigneeId == null) {
            return new FormerResult("SUCCESS", 1, "缺少必须参数", null);
        }
        //查询用户月卡
        Order_monthCardVo monthCardOrder = orderMapper.getMonthCatdOrderByUserId(userId);
        //判断月卡是否到期
        if(monthCardOrder==null || monthCardOrder.equals("")){
            return new FormerResult("SUCCESS", 1, "您的月卡已经到期或使用次数不够,请联系客服", null);
        }else {
            //封装预约参数
            Map<String, Object> reservationMap = new HashMap<String, Object>();
            reservationMap.put("userId", userId);
            reservationMap.put("userName", userName);
            reservationMap.put("takeConsigneeId", takeConsigneeId);
            reservationMap.put("orderNumber", monthCardOrder.getOrderNumber());
            //创建预约
            Integer insertId = laundryAppointmentService.createWashingAppoint(reservationMap);
            //封装月卡预约商品数据
            JSONArray jsonArray = JSONArray.fromObject(reservationJson);
            List<ItemJSON> itemJSONList = new ArrayList<>();
            for (int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                ItemJSON itemJSON = new ItemJSON();
                itemJSON.setItemId(Integer.parseInt(jsonObject.get("itemId").toString()));
                itemJSON.setQuentity(Integer.parseInt(jsonObject.get("quentity").toString()));
                //查询商品信息
                AppItemVo item = itemMapper.getItemInfoByItemId(Integer.parseInt(jsonObject.get("itemId").toString()));
                itemJSON.setItemName(item.getItemName());
                itemJSON.setPicture(item.getPicture());
                itemJSON.setPrice(item.getPrice());
                itemJSON.setOrderNumber(monthCardOrder.getOrderNumber());
                itemJSON.setReservationId(insertId);
                itemJSONMapper.addOrderJSONOnly(itemJSON);
                //装到集合做更新
                itemJSONList.add(itemJSON);
            }
            //解析预约商品
            if(monthCardOrder.getUserMonthCardCount()>=1){
                //如果月卡使用是最后一次
                if(monthCardOrder.getUserMonthCardCount()-1 ==0){
                    //修改月卡为不可用
                    orderMapper.updateUserMonthCardActive(monthCardOrder.getOrderNumber());
                }
                //更新月卡使用情况
                orderService.updateUserMonthCardCount(monthCardOrder.getUserMonthCardCount()-1,monthCardOrder.getOrderNumber());
                //更新月卡剩余可洗内容
                //解析商品json
                List<ItemJSON> itemJSONListMany  = itemJSONMapper.getItemJSONByOrderNumber(monthCardOrder.getOrderNumber());
                for (ItemJSON itemJSON : itemJSONList) {
                    for(ItemJSON itemJSON1 : itemJSONListMany){
                        if(itemJSON1.getItemId().equals(itemJSON.getItemId())){
                            Map<String, Object> params = new HashMap<>();
                            params.put("orderNumber", itemJSON1.getOrderNumber());
                            params.put("itemId", itemJSON1.getItemId());
                            params.put("quentity", itemJSON1.getQuentity() - itemJSON.getQuentity());
                            itemJSONMapper.updateJSONManyByOrderNumberAndItemId(params);
                        }else {
                            continue;
                        }
                    }
                }
                //如果月卡商品使用完毕，则更新月卡为不可用
                int count = 0;
                for (ItemJSON itemJSON : itemJSONList) {
                   if(itemJSON.getQuentity()>0){
                       count +=1;
                   }
                }
                if(count==0){
                    orderMapper.updateUserMonthCardActive(monthCardOrder.getOrderNumber());
                }
            }else {
                return new FormerResult("SUCCESS", 1, "您本月月卡使用次数已完结，请下个月再重试", null);
            }
        }
        return new FormerResult("SUCCESS", 0, "本次预约已成功！请等待小哥收件", null);
    }

    /**
     * 预约家政
     * @return
     */
    @RequestMapping(value = "/createHouseServiceAppoint",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult createHouseServiceAppoint(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        String userName = request.getParameter("userName");
        Integer takeConsigneeId = Integer.parseInt(request.getParameter("takeConsigneeId"));
        String visitTime = request.getParameter("visitTime");
        String reservationJson = request.getParameter("reservationJson");
        //效验必须参数
        if (userId == null || userName == null || takeConsigneeId == null || visitTime == null) {
            return new FormerResult("SUCCESS", 1, "缺少必须参数", null);
        }
        //查询用户定制的家政
        Order_custom_houseServiceVo customHouseServiceOrder = orderMapper.getCustomHouseServiceOrderByUserId(userId);
        //判断定制家政是否可用
        if(customHouseServiceOrder.getActive() ==0){
            return new FormerResult("SUCCESS", 1, "您的月卡已经到期或使用次数不够,请联系客服", null);
        }else {
            //效验预约时间是否符合购买时的选择时间
            int visitTimeSubYear = Integer.parseInt(visitTime.substring(0, 4));
            int visitTimeSubMonth = Integer.parseInt(visitTime.substring(5, 7));
            int visitTimeSubDay = Integer.parseInt(visitTime.substring(8,10));
            int week = DateUtils.CalculateWeekDay(visitTimeSubYear,visitTimeSubMonth,visitTimeSubDay);
            if (customHouseServiceOrder.getWorkTime()==1){
                if(week>=6){
                    return new FormerResult("SUCCESS", 1, "您好，您购买的定制家政不能在该时间预约！,请联系客服", null);
                }
            }else if(customHouseServiceOrder.getWorkTime()==2){
                if(week<=6){
                    return new FormerResult("SUCCESS", 1, "您好，您购买的定制家政不能在该时间预约！,请联系客服", null);
                }
            }
            //封装预约参数
            Map<String, Object> reservationMap = new HashMap<String, Object>();
            reservationMap.put("userId", userId);
            reservationMap.put("userName", userName);
            reservationMap.put("takeConsigneeId", takeConsigneeId);
            reservationMap.put("visitTime", visitTime);
            reservationMap.put("orderNumber", customHouseServiceOrder.getOrderNumber());
            //创建预约
            Integer insertId = laundryAppointmentService.createHouseServiceAppoint(reservationMap);
            //封装定制家政预约商品数据
            JSONArray jsonArray = JSONArray.fromObject(reservationJson);
            List<ItemJSON> itemJSONList = new ArrayList<>();
            for (int i=0;i<jsonArray.size();i++){
                ItemJSON itemJSON = new ItemJSON();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                itemJSON.setItemId(Integer.parseInt(jsonObject.get("itemId").toString()));
                itemJSON.setQuentity(Integer.parseInt(jsonObject.get("quentity").toString()));
                //查询商品信息
                AppItemVo item = itemMapper.getItemInfoByItemId(Integer.parseInt(jsonObject.get("itemId").toString()));
                itemJSON.setItemName(item.getItemName());
                itemJSON.setPicture(item.getPicture());
                itemJSON.setPrice(item.getPrice());
                itemJSON.setOrderNumber(customHouseServiceOrder.getOrderNumber());
                itemJSON.setReservationId(insertId);
                itemJSONMapper.addOrderJSONOnly(itemJSON);
                //装到集合做更新
                itemJSONList.add(itemJSON);
            }
            if(customHouseServiceOrder.getBaseServiceCount()>=1){
                //如果定制家政使用是最后一次
                if(customHouseServiceOrder.getBaseServiceCount()-1 ==0){
                    //修改定制家政为不可用
                    orderMapper.updateUserHouseServiceActive(customHouseServiceOrder.getOrderNumber());
                }
                //更新定制家政基础使用次数
                orderService.updateUserHouseServiceBaseServiceCount(customHouseServiceOrder.getBaseServiceCount()-1,customHouseServiceOrder.getOrderNumber());
                //更新定制家政剩余内容
                //解析商品json
                List<ItemJSON> itemJSONListMany  = itemJSONMapper.getItemJSONByOrderNumber(customHouseServiceOrder.getOrderNumber());
                for (ItemJSON itemJSON : itemJSONList) {
                    for(ItemJSON itemJSON1 : itemJSONListMany){
                        if(itemJSON1.getItemId().equals(itemJSON.getItemId())){
                            Map<String, Object> params = new HashMap<>();
                            params.put("orderNumber", itemJSON1.getOrderNumber());
                            params.put("itemId", itemJSON1.getItemId());
                            params.put("quentity", itemJSON1.getQuentity() - itemJSON.getQuentity());
                            itemJSONMapper.updateJSONManyByOrderNumberAndItemId(params);
                        }else {
                            continue;
                        }
                    }
                }
            }else {
                return new FormerResult("SUCCESS", 1, "您本月月卡使用次数已完结，请下个月再重试", null);
            }
        }
        return new FormerResult("SUCCESS", 0, "本次预约已成功！请等待家政人员上门服务", null);
    }

    /**
     * 开通关闭自动续费
     *
     * @param option
     * @return
     */

    /*public FormerResult changeAuto(BuyCardOptionVo option) {
        FormerResult result = new FormerResult();
        if (option.getIsAuto() == null || option.getUserId() == null ||
                option.getIsAuto() < 1 || option.getIsAuto() > 2) {
            return CommonUtil.FAIL(result,"缺少参数!",null);
        }
        return CommonUtil.SUCCESS(result,"更新成功!",laundryAppointmentService.changeAuto(option));
    }*/
}