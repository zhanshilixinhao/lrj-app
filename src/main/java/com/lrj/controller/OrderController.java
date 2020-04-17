package com.lrj.controller;

import com.lrj.VO.OrderVo;
import com.lrj.VO.ResultVo;
import com.lrj.constant.Constant;
import com.lrj.service.IOrderService;
import com.lrj.util.DateUtils;
import com.lrj.util.RandomUtil;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.logging.ConsoleHandler;

/**
 * @author : cwj
 * @describe : 订单
 * @date : 2020-4-10
 */
@RestController
public class OrderController {

    @Resource
    private IOrderService orderService;
    /**
     * 生成订单
     */
    @RequestMapping(value = "/createOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo createOrder(OrderVo orderVo,Integer userId,Integer orderType){
        // 生成订单号
        String orderNum = DateUtils.getNewdateBygs("yyyyMMdd") + RandomUtil.generateOrder(6);
        // 处理订单
        orderVo.setAppOrderId(Integer.parseInt(orderNum));
        orderVo.setUserId(userId);
        orderVo.setOrderType(orderType);
        orderVo.setStatus(Constant.ORDER_STATUS_UNFINISHED);
        orderVo.setPayStatus(Constant.ORDER_PAYSTATUS_NOPAY);
       // orderVo.setIsLock(Constant.ORDER_ISLOCK_NO);
        orderVo.setCreateTime(DateUtils.getNowtime());
       Integer crateNum = orderService.createOrder(orderVo);
        return null;
    }
}
