package com.lrj.service;

import com.lrj.VO.BuyCardOptionVo;
import com.lrj.VO.FormerResult;

import java.util.Map;

/**
 * @author Lxh
 * @date 2020/4/17 17:07
 */
public interface LaundryAppointmentService {
    /**
     * 创建 洗衣 预约
     * @param reservationMap
     * @return
     */
    int createWashingAppoint(Map<String,Object> reservationMap);

    /**
     * 创建 家政 预约
     * @param reservationMap
     */
    int createHouseServiceAppoint(Map<String, Object> reservationMap);
}
