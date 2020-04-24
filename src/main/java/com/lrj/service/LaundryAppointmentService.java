package com.lrj.service;

import com.lrj.VO.BuyCardOptionVo;
import com.lrj.VO.FormerResult;

/**
 * @author Lxh
 * @date 2020/4/17 17:07
 */
public interface LaundryAppointmentService {
    FormerResult createAppoint(BuyCardOptionVo option);

    Object changeAuto(BuyCardOptionVo option);

    FormerResult getList(BuyCardOptionVo option);
}
