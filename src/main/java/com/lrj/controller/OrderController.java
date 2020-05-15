package com.lrj.controller;

import com.lrj.VO.*;
import com.lrj.constant.Constant;
import com.lrj.mapper.IHouseServiceMapper;
import com.lrj.mapper.IMonthCardMapper;
import com.lrj.mapper.IOrderMapper;
import com.lrj.pojo.MonthCard;
import com.lrj.service.IOrderService;
import com.lrj.service.IShoppingService;
import com.lrj.service.IUserService;
import com.lrj.util.DateUtils;
import com.lrj.util.RandomUtil;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Resource
    private IUserService userService;
    @Resource
    private IShoppingService shoppingService;
    @Resource
    private IOrderMapper orderMapper;
    @Resource
    private IMonthCardMapper monthCardMapper;
    @Resource
    private IHouseServiceMapper houseServiceMapper;

    /**
     * @param request
     * @return
     */
    @RequestMapping(value = "/createOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult createOrder(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        BigDecimal totalPrice = new BigDecimal(request.getParameter("totalPrice"));
        Integer orderType = Integer.parseInt(request.getParameter("orderType"));
        OrderVo orderVo = new OrderVo();
        // 生成订单号
        String orderNum = DateUtils.getNewdateBygs("yyyyMMdd") + RandomUtil.generateOrder(6);
        // 处理订单
        orderVo.setOrderNumber(orderNum);
        orderVo.setUserId(userId);
        orderVo.setOrderType(orderType);
        orderVo.setStatus(Constant.ORDER_STATUS_UNFINISHED);
        orderVo.setPayStatus(Constant.ORDER_PAYSTATUS_NOPAY);
        orderVo.setCreateTime(DateUtils.getNowtime());
        Integer couponId = Integer.parseInt(request.getParameter("couponId"));
        //是否使用红包
        if(couponId >0){
            orderVo.setUserCouponId(couponId);
        }
        orderVo.setTotalPrice(totalPrice);
       Integer createNum = orderService.createOrder(orderVo,request);
       //订单创建成功后处理业务
        if(createNum ==1){
            userService.updateCoupon(couponId);
            shoppingService.emptyShopCart(userId);
        }
        OrderVo orderVo1 = orderService.findOrderByOrderNumber(orderVo.getOrderNumber());
        return new FormerResult("success", 0, "下单成功", orderVo1);
    }

    /**
     * 查询订单
     */
    @RequestMapping(value = "/findUserOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo findUserOrder(Integer userId){
        /** 校验必须参数 **/
        if (userId == null || userId == 0) {
            return new ResultVo("success", 1, "参数有误,请检查参数", null);
        }
        List<OrderVo> orderVoList = orderService.findOrderListByUserId(userId);
        //订单分类
        List<Object> userOrderList = new ArrayList<Object>();
        for (OrderVo orderVo : orderVoList){
            switch (orderVo.getOrderType()){
                case 1:
                    Order_washingVo washingOrder = orderMapper.getWashingOrderByOrderNumber(orderVo.getOrderNumber());
                    userOrderList.add(washingOrder);
                    break;
                case 2:
                    Order_monthCardVo monthCardOrder = orderMapper.getMonthCatdOrder(orderVo.getOrderNumber());
                    //拼接用户信息
                    UserInfoVo userInfoVo = userService.findUserInfoByUserId(orderVo.getUserId());
                    monthCardOrder.setUserheadPhoto(userInfoVo.getHeadPhoto());
                    //拼接月卡具体信息
                    MonthCard monthCard = monthCardMapper.getMonthCardById(monthCardOrder.getMonthCardId());
                    monthCardOrder.setMonthCard(monthCard);
                    userOrderList.add(monthCardOrder);
                    break;
                case 3:
                    Order_houseServiceVo houseServiceOrder = orderMapper.getHouseServiceByOrderNumber(orderVo.getOrderNumber());
                    //拼接家政服务具体信息
                    HouseServiceVo houseServiceVo =  houseServiceMapper.getHouseServiceByItemId(houseServiceOrder.getHouseServiceId());
                    houseServiceOrder.setHouseServiceVo(houseServiceVo);
                    userOrderList.add(houseServiceOrder);
                    break;
                case 4:
                    Order_custom_houseServiceVo customHouseServiceOrder = orderMapper.getCustomHouseServiceByOrderNumber(orderVo.getOrderNumber());
                    break;
            }
        }
        return new ResultVo("SUCCESS", 0, "查询成功", null);
    }
}
