package com.lrj.controller;

import com.lrj.VO.FormerResult;
import com.lrj.VO.OrderVo;
import com.lrj.VO.ResultVo;
import com.lrj.VO.ShoppingVo;
import com.lrj.constant.Constant;
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
    /**
     * 生成订单
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
}
