package com.lrj.controller;

import com.lrj.VO.*;
import com.lrj.constant.Constant;
import com.lrj.mapper.IHouseServiceMapper;
import com.lrj.mapper.IMonthCardMapper;
import com.lrj.mapper.IOrderMapper;
import com.lrj.mapper.ReservationMapper;
import com.lrj.pojo.MonthCard;
import com.lrj.pojo.Order;
import com.lrj.pojo.Reservation;
import com.lrj.service.IOrderService;
import com.lrj.service.IShoppingService;
import com.lrj.service.IUserService;
import com.lrj.util.DateUtils;
import com.lrj.util.RandomUtil;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private ReservationMapper reservationMapper;
    @Resource
    private IMonthCardMapper monthCardMapper;
    @Resource
    private IHouseServiceMapper houseServiceMapper;

    /** 创建订单  +增值服务
     * @param request
     * @return
     */
    @RequestMapping(value = "/createOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult createOrder(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        BigDecimal totalPrice = new BigDecimal(request.getParameter("totalPrice")).setScale(2, RoundingMode.FLOOR); //实际支付金
        BigDecimal originalPrice = new BigDecimal(request.getParameter("originalPrice")); //原价金额
        BigDecimal activityPrice = null; //活动减免金额
        Integer orderType = Integer.parseInt(request.getParameter("orderType"));
        Integer activityId = Integer.parseInt(request.getParameter("activityId"));

        OrderVo orderVo = new OrderVo();
        //是否参与活动
        if(activityId !=null && activityId !=0){
            //
            orderVo.setActivity(activityId);
            orderVo.setActivityPrice(activityPrice);
        }else {
            totalPrice = totalPrice;
            orderVo.setActivity(0);
            orderVo.setActivityPrice(new BigDecimal(0));
        }
        // 生成订单号
        String orderNum = DateUtils.getNewdateBygs("yyyyMMdd") + RandomUtil.generateOrder(6);
        // 处理订单
        orderVo.setOrderNumber(orderNum);
        orderVo.setUserId(userId);
        orderVo.setOrderType(orderType);
        orderVo.setStatus(Constant.ORDER_STATUS_UNFINISHED);
        orderVo.setPayStatus(Constant.ORDER_PAYSTATUS_NOPAY);
        orderVo.setCreateTime(DateUtils.getNowtime());
        orderVo.setOriginalPrice(originalPrice);
        String couponId = request.getParameter("couponId");
        Integer couponIdInt = 0;
        //是否使用红包
        if(couponId.equals("") || couponId==null || couponId.equals("0")){
            orderVo.setUserCouponId(couponIdInt);
        }else {
            couponIdInt = Integer.parseInt(couponId);
            orderVo.setUserCouponId(Integer.parseInt(couponId));
        }
        orderVo.setTotalPrice(totalPrice);
        Integer createNum = orderService.createOrder(orderVo,request);
        //订单创建成功后处理业务
        if(createNum ==1){
            //更新红包
            userService.updateCoupon(couponIdInt);
            //邀请新用户下单赠送红包
            List<OrderVo> orderVoList=  orderService.findOrderListByUserId(userId);
            if(orderVoList.size()==1){
                //赠送随机红包（1-10元：通用类型）
                UserInfoVo userInfoVo = userService.findUserInfoByUserId(userId);
                userService.sendRandomCoupon(userInfoVo.getSuperId(),4);
            }
        }
        OrderVo orderVo1 = orderService.findOrderByOrderNumber(orderVo.getOrderNumber());

        return new FormerResult("success", 0, "下单成功", orderVo1);
    }

    /**
     * 查询订单(预约服务)
     */
    @RequestMapping(value = "/findUserReservation",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo findUserOrder(Integer userId){
        /** 校验必须参数 **/
        if (userId == null || userId == 0) {
            return new ResultVo("success", 1, "参数有误,请检查参数", null);
        }
        List<Reservation> reservationList =reservationMapper.getReservationListByUserId(userId);

        return new ResultVo("SUCCESS", 0, "查询成功", reservationList);
    }

    /**
     *  查询月卡订单(根据type：1：可用月卡   2：全部月卡)
     */
    @RequestMapping(value = "/findUserMonthCardOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult findUserMonthCardOrder(Integer userId,Integer type){
        //效验必须参数
        if (userId == null || userId==0 || type==null || type==0) {
            return new FormerResult("SUCCESS", 1, "缺少必须参数", null);
        }
        if(type==1){
            Order_monthCardVo monthCardVo = orderMapper.getMonthCatdOrderByUserId(userId);
            if(monthCardVo == null || monthCardVo.equals("")){
                return new FormerResult("SUCCESS", 0, "查询完成", monthCardVo);
            }else {
                //拼接月卡名字
                MonthCard monthCard = monthCardMapper.getMonthCardById(monthCardVo.getMonthCardId());
                monthCardVo.setMonthCardName(monthCard.getName());
                //转化json 为JSONArray  供前端使用
                JSONArray userMonthCardItemList = JSONArray.fromObject(monthCardVo.getUserMonthCardItemJson());
                monthCardVo.setUserMonthCardItemJSONArray(userMonthCardItemList);
                return new FormerResult("SUCCESS",0,"查询成功！",monthCardVo);
            }
        }else if(type==2){
            List<Order_monthCardVo> monthCardOrderList = orderMapper.getMonthCatdOrderListByUserId(userId);
            if(monthCardOrderList == null || monthCardOrderList.equals("")){
                return new FormerResult("SUCCESS", 0, "查询完成", monthCardOrderList);
            }else {
                for (Order_monthCardVo monthCardOrderVo : monthCardOrderList) {
                    MonthCard monthCard = monthCardMapper.getMonthCardById(monthCardOrderVo.getMonthCardId());
                    monthCardOrderVo.setMonthCardName(monthCard.getName());
                    //转化json 为JSONArray  供前端使用
                    JSONArray userMonthCardItemList = JSONArray.fromObject(monthCardOrderVo.getUserMonthCardItemJson());
                    monthCardOrderVo.setUserMonthCardItemJSONArray(userMonthCardItemList);
                }
                return new FormerResult("SUCCESS", 0, "查询成功！", monthCardOrderList);
            }
        }
        return null;
    }

    /**
     * 查询用户的定制家政订单
     */
    @RequestMapping(value = "/findCustomHouseServiceOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult findCustomHouseServiceOrder(Integer userId,Integer type){
        //效验必须参数
        if (userId == null || userId==0 || type==null || type==0) {
            return new FormerResult("SUCCESS", 1, "缺少必须参数", null);
        }
        if(type==1){
            Order_custom_houseServiceVo customHouseServiceVo = orderMapper.getCustomHouseServiceOrderByUserId(userId);
            if(customHouseServiceVo == null || customHouseServiceVo.equals("")){
                return new FormerResult("SUCCESS", 0, "查询完成", customHouseServiceVo);
            }else {
                //转化json 为JSONArray  供前端使用
                JSONArray individualServiceJSONArray= JSONArray.fromObject(customHouseServiceVo.getIndividualServiceJson());
                customHouseServiceVo.setIndividualServiceJSONArray(individualServiceJSONArray);
                return new FormerResult("SUCCESS",0,"查询成功！",customHouseServiceVo);
            }
        }else if(type==2){
            List<Order_custom_houseServiceVo> customHouseServiceOrderList = orderMapper.getCustomHouseServiceOrderListByUserId(userId);
            if(customHouseServiceOrderList == null || customHouseServiceOrderList.equals("")){
                return new FormerResult("SUCCESS", 0, "查询完成", customHouseServiceOrderList);
            }else {
                for(Order_custom_houseServiceVo customHouseServiceOrderVo : customHouseServiceOrderList) {
                    //转化json 为JSONArray  供前端使用
                    JSONArray individualServiceJSONArray = JSONArray.fromObject(customHouseServiceOrderVo.getIndividualServiceJson());
                    customHouseServiceOrderVo.setIndividualServiceJSONArray(individualServiceJSONArray);
                }
                return new FormerResult("SUCCESS", 0, "查询完成", customHouseServiceOrderList);
            }
        }
       return null;
    }
}
