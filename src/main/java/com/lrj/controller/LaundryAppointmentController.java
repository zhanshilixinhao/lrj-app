package com.lrj.controller;
import com.lrj.VO.FormerResult;
import com.lrj.VO.Order_custom_houseServiceVo;
import com.lrj.VO.Order_monthCardVo;
import com.lrj.mapper.IOrderMapper;
import com.lrj.service.IOrderService;
import com.lrj.service.LaundryAppointmentService;

import com.lrj.util.BigDecimalUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
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

    /**
     * 预约洗衣
     * @param request
     * @return
     */
    @RequestMapping(value = "/createWashingAppoint",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult createWashingAppoint(HttpServletRequest request) { Integer userId = Integer.parseInt(request.getParameter("userId"));
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
            reservationMap.put("reservationJson", reservationJson);

            //创建预约
            laundryAppointmentService.createWashingAppoint(reservationMap);
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
                JSONArray wahsingDetailJSONArray = JSONArray.fromObject(reservationJson);
                JSONArray washingDetailJSONArrayAll = JSONArray.fromObject(monthCardOrder.getUserMonthCardItemJson());
                for (int i = 0; i < wahsingDetailJSONArray.size(); i++) {
                    JSONObject d = wahsingDetailJSONArray.getJSONObject(i);
                    for (int j = 0; j < washingDetailJSONArrayAll.size(); j++) {
                        JSONObject D = washingDetailJSONArrayAll.getJSONObject(j);
                        if (d.get("itemId").equals(D.get("itemId"))) {
                           int num1 =  Integer.parseInt(D.get("count").toString());
                            int num2 = Integer.parseInt(d.get("quantity").toString());
                            int end = num1-num2;
                            D.put("count", end);
                        }
                    }
                }
                //保存更新
                orderService.updateUserMonthCardItemJson(washingDetailJSONArrayAll.toString(),monthCardOrder.getOrderNumber());
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
            //封装预约参数
            Map<String, Object> reservationMap = new HashMap<String, Object>();
            reservationMap.put("userId", userId);
            reservationMap.put("userName", userName);
            reservationMap.put("takeConsigneeId", takeConsigneeId);
            reservationMap.put("visitTime", visitTime);
            reservationMap.put("orderNumber", customHouseServiceOrder.getOrderNumber());
            reservationMap.put("reservationJson", reservationJson);
            //创建预约
            laundryAppointmentService.createHouseServiceAppoint(reservationMap);
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
                JSONArray customHouseServiceDetailJSONArray = JSONArray.fromObject(reservationJson);
                JSONArray customHouseServiceDetailJSONArrayAll = JSONArray.fromObject(customHouseServiceOrder.getIndividualServiceJson());
                for (int i = 0; i < customHouseServiceDetailJSONArray.size(); i++) {
                    JSONObject d = customHouseServiceDetailJSONArray.getJSONObject(i);
                    for (int j = 0; j < customHouseServiceDetailJSONArrayAll.size(); j++) {
                        JSONObject D = customHouseServiceDetailJSONArrayAll.getJSONObject(j);
                        if (d.get("itemId").equals(D.get("itemId"))) {
                            int num1 =  Integer.parseInt(D.get("quantity").toString());
                            int num2 = Integer.parseInt(d.get("quantity").toString());
                            int end = num1-num2;
                            D.put("quantity", end);
                        }
                    }
                }
                //保存更新
                orderService.updateIndividualServiceJson(customHouseServiceDetailJSONArrayAll.toString(),customHouseServiceOrder.getOrderNumber());
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
    /**
     * 获取预约列表
     *
     * @param option
     * @return
     */
    /*public FormerResult getList(BuyCardOptionVo option) {
        FormerResult result = new FormerResult();
        if (option.getUserId() == null) {
            return CommonUtil.FAIL(result,"缺少参数",null);
        }
        if (option.getPageSize() == null) {
            option.setPageSize(10);
        }
        if (option.getCurrentPage() == null) {
            option.setCurrentPage(1);
        }
        option.setCurrentPage((option.getCurrentPage() - 1) * option.getPageSize());
        return laundryAppointmentService.getList(option);
    }*/
}