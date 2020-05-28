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

    public FormerResult createWashingAppoint(Map<String,Object> reservationMap) {
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
        reservation.setOrderType(orderVo.getOrderType());
        //其他信息
        reservation.setGrabOrderId(null);
        reservation.setStatus(orderVo.getPayStatus());
        reservation.setTrackingStatus(Constant.ORDER_TRANSSTATUS_LOCK);
        reservation.setCreateTime(DateUtils.getNowDateTime());
        reservation.setUpdateTime(DateUtils.getNowDateTime());
        reservation.setUserId(Integer.parseInt(reservationMap.get("userId").toString()));
        Integer insertNumber = reservationMapper.insertReservation(reservation);
        return new FormerResult("SUCCESS", 0, "预约成功！", null);
    }

    public FormerResult createHouseServiceAppoint(Map<String, Object> reservationMap) {
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
        reservation.setOrderType(orderVo.getOrderType());
        //其他信息
        reservation.setGrabOrderId(null);
        reservation.setStatus(orderVo.getPayStatus());
        reservation.setTrackingStatus(Constant.ORDER_TRANSSTATUS_LOCK);
        Integer insertNumber = reservationMapper.insertReservation(reservation);
        return new FormerResult("SUCCESS", 0, "预约成功！", null);
    }
}
