package com.lrj.service.impl;

import com.lrj.VO.*;
import com.lrj.constant.Constant;
import com.lrj.mapper.*;
import com.lrj.pojo.*;
import com.lrj.service.LaundryAppointmentService;
import com.lrj.util.CommonUtil;
import com.lrj.util.DateUtils;
import com.lrj.util.RandomUtil;
import org.springframework.stereotype.Service;
import sun.rmi.server.InactiveGroupException;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Lxh
 * @date 2020/4/17 17:08
 */
@Service
public class LaundryAppointmentServiceImpl implements LaundryAppointmentService{

    @Resource
    private ConsigneeMapper consigneeMapper;
    @Resource
    private IOrderMapper orderMapper;
    @Resource
    private ReservationMapper reservationMapper;

    public int createWashingAppoint(Map<String,Object> reservationMap) {
        //创建预约信息
        Reservation reservation = new Reservation();
        //拼接地址信息
        Integer takeConsigneeId = Integer.parseInt(reservationMap.get("takeConsigneeId").toString());
        Consignee consignee = consigneeMapper.getConsigneeByConsigneeId(takeConsigneeId);
        reservation.setAddress(consignee.getAddress());
        reservation.setLatitude(consignee.getLatitude());
        reservation.setLongitude(consignee.getLongitude());
        //拼接订单信息
        String orderNumber = reservationMap.get("orderNumber").toString();
        OrderVo orderVo = orderMapper.getOrderByOrderNumber(orderNumber);
        reservation.setOrderNumber(reservationMap.get("orderNumber").toString());
        //洗衣预约
        if(orderVo.getOrderType() ==1) {
            Order_washingVo washingOrderVo = orderMapper.getWashingOrderByOrderNumber(orderNumber);
            reservation.setOrderType(orderVo.getOrderType());
            reservation.setReservationJson(washingOrderVo.getShoppingJson());
            reservation.setGetClothesTime(reservationMap.get("takeTime").toString());
            //服务费判断
            if(washingOrderVo.getServicePrice().doubleValue()==12.00){
                reservation.setIsService(1);
            }else {
                reservation.setIsService(0);
            }
            //加急费判断
            if(washingOrderVo.getUrgentPrice().doubleValue()==50.00){
                reservation.setIsUrgent(1);
            }else {
                reservation.setIsUrgent(0);
            }
            //总价
            reservation.setTotalPrice(orderVo.getTotalPrice());
        //月卡预约
        }else if(orderVo.getOrderType() ==2){
            reservation.setOrderType(orderVo.getOrderType());
            reservation.setTotalPrice(new BigDecimal(0.00));
        }

        //其他信息
        reservation.setGrabOrderIdSend(null);
        reservation.setIsEnd(0);

        reservation.setTrackingStatus(Constant.ORDER_TRANSSTATUS_LOCK);
        if(orderVo.getPayStatus()==0){
            reservation.setStatus(2);
        }else if(orderVo.getPayStatus()==1){
            reservation.setStatus(3);
        }
        reservation.setCreateTime(DateUtils.getNowDateTime());
        reservation.setUpdateTime(DateUtils.getNowDateTime());
        reservation.setUserId(Integer.parseInt(reservationMap.get("userId").toString()));
        reservationMapper.insertReservation(reservation);
       return reservation.getReservationId();
    }

    @Override
    public int createHouseServiceAppoint(Map<String, Object> reservationMap) {
        //创建预约信息
        Reservation reservation = new Reservation();
        //拼接地址信息
        Integer takeConsigneeId = Integer.parseInt(reservationMap.get("takeConsigneeId").toString());
        Consignee consignee = consigneeMapper.getConsigneeByConsigneeId(takeConsigneeId);
        reservation.setAddress(consignee.getAddress());
        reservation.setLatitude(consignee.getLatitude());
        reservation.setLongitude(consignee.getLongitude());
        //拼接订单信息
        String orderNumber = reservationMap.get("orderNumber").toString();
        OrderVo orderVo = orderMapper.getOrderByOrderNumber(orderNumber);
        reservation.setOrderNumber(reservationMap.get("orderNumber").toString());
        //单项家政预约
        if(orderVo.getOrderType() ==3) {
            Order_houseServiceVo houseServiceOrderVo = orderMapper.getHouseServiceByOrderNumber(orderNumber);
            reservation.setOrderType(orderVo.getOrderType());
            reservation.setStatus(houseServiceOrderVo.getPayStatus());
            reservation.setTotalPrice(orderVo.getTotalPrice());
        //定制家政预约
        }else if(orderVo.getOrderType() ==4){
            reservation.setOrderType(orderVo.getOrderType());
            reservation.setStatus(1);
            reservation.setTotalPrice(orderVo.getTotalPrice());
            reservation.setGetClothesTime((String) reservationMap.get("visitTime"));
        }
        //其他信息
        reservation.setGrabOrderIdTake(null);
        reservation.setIsEnd(0);
        reservation.setTrackingStatus(Constant.ORDER_TRANSSTATUS_LOCK);
        if(orderVo.getPayStatus()==0){
            reservation.setStatus(2);
        }else if(orderVo.getPayStatus()==1){
            reservation.setStatus(3);
        }
        reservation.setCreateTime(DateUtils.getNowDateTime());
        reservation.setUpdateTime(DateUtils.getNowDateTime());
        reservation.setUserId(Integer.parseInt(reservationMap.get("userId").toString()));
        reservation.setIsService(0);
        reservation.setIsUrgent(0);

        reservation.setTrackingStatus(Constant.ORDER_TRANSSTATUS_LOCK);
        Integer insertNumber = reservationMapper.insertReservation(reservation);
        return reservation.getReservationId();
    }
}
