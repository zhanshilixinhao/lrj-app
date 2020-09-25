package com.lrj.controller;

import com.alipay.api.domain.ItemInfo;
import com.lrj.VO.*;
import com.lrj.constant.Constant;
import com.lrj.mapper.*;
import com.lrj.pojo.*;
import com.lrj.service.IOrderService;
import com.lrj.service.IShoppingService;
import com.lrj.service.IUserService;
import com.lrj.util.DateUtils;
import com.lrj.util.RandomUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
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
    @Resource
    private IItemMapper itemMapper;
    @Resource
    private IItemJSONMapper itemJSONMapper;
    @Resource
    private IActivityMapper iActivityMapper;

    /** 创建订单  +增值服务
     * @param request
     * @return
     */
    @RequestMapping(value = "/createOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult createOrder(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        Integer merchantId = Integer.parseInt(request.getParameter("merchantId"));
        Integer couponIdInt = 0;
        Integer platform = Integer.parseInt(request.getParameter("platform"));

        OrderVo orderVo = new OrderVo();
        // 生成订单号
        String orderNum = DateUtils.getNewdateBygs("yyyyMMdd") + RandomUtil.generateOrder(6);
        // 处理订单(共同参数)
        orderVo.setOrderNumber(orderNum);
        orderVo.setUserId(userId);
        orderVo.setCreateTime(DateUtils.getNowtime());
        orderVo.setPlatform(platform);
        //建立不同类型订单，不同参数Map
        Map<String, Object> typeParams = new HashMap<>();
        //如果是销售商家订单，替换order数据来源（不同参数）
        if(merchantId !=null && merchantId!=0){
            orderVo.setMerchantId(merchantId);
            //指定创建的订单数据源
            Integer merchantOrderId = Integer.parseInt(request.getParameter("merchantOrderId"));
            Integer takeConsigneeId = null;//单项洗衣和单项家政才有
            MerchantOrder merchantOrder = orderMapper.getMerchantOrderById(merchantOrderId);
            orderVo.setActivity(0);
            orderVo.setActivityPrice(merchantOrder.getActivityPrice());
            orderVo.setOrderType(merchantOrder.getOrderType());
            orderVo.setStatus(Constant.ORDER_STATUS_PAY);
            orderVo.setPayStatus(1);
            orderVo.setUserCouponId(couponIdInt);
            orderVo.setOriginalPrice(merchantOrder.getOriginalPrice());
            orderVo.setUserCouponId(couponIdInt);
            orderVo.setTotalPrice(merchantOrder.getTotalPrice());
            switch (merchantOrder.getOrderType()){
                case 1:
                    BigDecimal levelPrice = new BigDecimal("0.00"); //等级减免金额
                    String takeTime = request.getParameter("takeTime");
                    BigDecimal urgentPrice = new BigDecimal("0.00");
                    BigDecimal servicePrice = new BigDecimal("0.00");
                    takeConsigneeId = Integer.parseInt(request.getParameter("takeConsigneeId"));
                    Integer sendConsigneeId = Integer.parseInt(request.getParameter("takeConsigneeId"));
                    typeParams.put("levelPrice", levelPrice);
                    typeParams.put("takeTime", takeTime);
                    typeParams.put("takeConsigneeId", takeConsigneeId);
                    typeParams.put("sendConsigneeId", sendConsigneeId);
                    typeParams.put("urgentPrice", urgentPrice);
                    typeParams.put("servicePrice", servicePrice);
                    typeParams.put("detailJson", merchantOrder.getDetailJson());
                    break;
                case 2:
                    Integer monthCardId = merchantOrder.getMonthCardId();
                    typeParams.put("monthCardId", monthCardId);
                    break;
                case 3:
                    takeConsigneeId =Integer.parseInt(request.getParameter("takeConsigneeId"));
                    String houseServiceJson = merchantOrder.getDetailJson();
                    typeParams.put("takeConsigneeId", takeConsigneeId);
                    typeParams.put("houseServiceJson", houseServiceJson);
                    break;
                case 4:
                    Integer serviceCycle = merchantOrder.getServiceCycle();
                    Integer baseServiceCount = merchantOrder.getBaseServiceCount();
                    Integer workTime = merchantOrder.getWorkTime();
                    String houseArea = merchantOrder.getHouseArea();
                    BigDecimal baseServicePrice = merchantOrder.getBaseServicePrice();
                    String individualServiceJson = merchantOrder.getDetailJson();
                    typeParams.put("serviceCycle", serviceCycle);
                    typeParams.put("baseServiceCount", baseServiceCount);
                    typeParams.put("workTime", workTime);
                    typeParams.put("houseArea", houseArea);
                    typeParams.put("baseServicePrice", baseServicePrice);
                    typeParams.put("individualServiceJson", individualServiceJson);
                    break;
            }
        //用户正常下单(包含运营活动下单)
        }else {
            String couponId = request.getParameter("couponId");
            BigDecimal totalPrice = new BigDecimal(request.getParameter("totalPrice")).setScale(2, RoundingMode.FLOOR); //实际支付金
            BigDecimal originalPrice = new BigDecimal(request.getParameter("originalPrice")); //原价金额
            BigDecimal activityPrice = null; //活动减免金额

            //是否参与活动
            Integer activityId = Integer.parseInt(request.getParameter("activityId"));
            if(activityId !=null && activityId !=0){
                switch (activityId){
                    case 14:  //商家引流活动
                        Activity activity = iActivityMapper.getActivityById(activityId);
                        totalPrice = activity.getPrice();
                        //将活动数据源转化为为商品数据源
                        List<ActivityJSON> activityJSONList = iActivityMapper.getActivityItemList(activityId);
                        List<ItemJSON> itemJSONList = new ArrayList<>();
                        for(ActivityJSON activityJSON : activityJSONList){
                            ItemJSON itemJSON = new ItemJSON();
                            itemJSON.setItemId(activityJSON.getItemId());
                            itemJSON.setItemUnit(activityJSON.getItemUnit());
                            itemJSON.setPicture(activityJSON.getPicture());
                            itemJSON.setPrice(activityJSON.getPrice());
                            itemJSON.setQuentity(activityJSON.getQuentity());
                            itemJSON.setItemName(activityJSON.getItemName());
                            itemJSONList.add(itemJSON);
                        }
                        typeParams.put("itemJSONList", itemJSONList);
                        break;
                    case 16:  //买一送一
                        //判断是分享者还是享受者
                        String shareOrderNumber = request.getParameter("shareOrderNumber");
                        //是享受者
                        if(shareOrderNumber !=null){
                            OrderVo orderVo1 = orderService.findOrderByOrderNumber(shareOrderNumber);
                            System.out.println("111111111:" + orderVo1.getIsShare());
                            if(orderVo1.getIsShare()==null){
                                orderVo.setShareOrderNumber(shareOrderNumber);
                                //设置该分享单不可再领取
                                orderService.updateOrderIsShare(1,shareOrderNumber);
                                //判断是新用户还是老用户
                                List<OrderVo> orderVoList=  orderService.findOrderListByUserId(userId);
                                if(orderVoList.size()>0){ //老用户
                                    /** 获取购物车商品列表 **/
                                    List<ShoppingVo> shoppingVoList = shoppingService.getShoppingDetails(userId);
                                    /**获取分享单的商品列表**/
                                    List<ItemJSON> washingOrderItemJSON = itemJSONMapper.getItemJSONByOrderNumberFromItemJSONOnly(shareOrderNumber);
                                    //比对商品减免价格
                                    for(ShoppingVo shoppingVo : shoppingVoList){
                                        //解析商品json
                                        for (int i = 0; i < washingOrderItemJSON.size(); i++) {
                                            ItemJSON d = washingOrderItemJSON.get(i);
                                            //如果商品相同
                                            if(shoppingVo.getItemId().equals(d.getItemId())){
                                                //判断数量
                                                if(shoppingVo.getQuantity()-d.getQuentity()==0){ //等于
                                                    double price1 = totalPrice.doubleValue();
                                                    double price2 = shoppingVo.getQuantity().doubleValue()*shoppingVo.getPrice().doubleValue();
                                                    totalPrice = new BigDecimal(price1 - price2);
                                                }else if(shoppingVo.getQuantity() - d.getQuentity()<0){ //小于
                                                    double price1 = totalPrice.doubleValue();
                                                    double price2 = shoppingVo.getQuantity().doubleValue()*shoppingVo.getPrice().doubleValue();
                                                    totalPrice = new BigDecimal(price1 - price2);
                                                }else if(shoppingVo.getQuantity() - d.getQuentity()>0){ //大于
                                                    double price1 = totalPrice.doubleValue();
                                                    double price2 = d.getQuentity().doubleValue()*d.getPrice().doubleValue();
                                                    totalPrice = new BigDecimal(price1-price2);
                                                }
                                            }else {
                                                continue;
                                            }
                                        }
                                    }
                                    //转化当减免金额=下单金额而不收费时，避免支付出错，强制0.01元
                                    if(totalPrice.doubleValue()==0){
                                        totalPrice = new BigDecimal(0.01);
                                    }
                                }else if(orderVoList.size()==0){ //新用户
                                    if(totalPrice.doubleValue() - orderVo1.getTotalPrice().doubleValue() >=0){  //减免全部或分享单金额
                                        totalPrice = new BigDecimal(totalPrice.doubleValue()-orderVo1.getTotalPrice().doubleValue());
                                    }else {
                                        totalPrice = new BigDecimal(0.01); //减免全部
                                    }
                                }
                            }else {
                                totalPrice = totalPrice;
                            }
                            //是分享者
                        }else {
                            totalPrice = totalPrice;
                        }
                        break;
                }
                orderVo.setActivity(activityId);
                orderVo.setActivityPrice(activityPrice);
            }else {
                totalPrice = totalPrice;
                orderVo.setActivity(0);
                orderVo.setActivityPrice(new BigDecimal(0));
            }
            System.out.println("本次使用的钱："+totalPrice);
            //是否使用红包
            if(couponId.equals("") || couponId==null || couponId.equals("0")){
                orderVo.setUserCouponId(couponIdInt);
            }else {
                couponIdInt = Integer.parseInt(couponId);
                orderVo.setUserCouponId(Integer.parseInt(couponId));
            }
            Integer orderType = Integer.parseInt(request.getParameter("orderType"));
            Integer takeConsigneeId = null;
            AppItemVo item = null;
            switch (orderType){
                case 1:
                    BigDecimal levelPrice = new BigDecimal(request.getParameter("levelPrice")); //等级减免金额
                    String takeTime = request.getParameter("takeTime");
                    takeConsigneeId = Integer.parseInt(request.getParameter("takeConsigneeId"));
                    Integer sendConsigneeId = Integer.parseInt(request.getParameter("takeConsigneeId"));
                    BigDecimal urgentPrice = new BigDecimal(request.getParameter("urgentPrice"));
                    BigDecimal servicePrice = new BigDecimal(request.getParameter("servicePrice"));
                    typeParams.put("levelPrice", levelPrice);
                    typeParams.put("takeTime", takeTime);
                    typeParams.put("takeConsigneeId", takeConsigneeId);
                    typeParams.put("sendConsigneeId", sendConsigneeId);
                    typeParams.put("urgentPrice", urgentPrice);
                    typeParams.put("servicePrice", servicePrice);
                    break;
                case 2:
                    Integer monthCardId = Integer.parseInt(request.getParameter("monthCardId"));
                    typeParams.put("monthCardId", monthCardId);
                    break;
                case 3:
                    takeConsigneeId =Integer.parseInt(request.getParameter("takeConsigneeId"));
                    Integer houseServiceId = Integer.parseInt(request.getParameter("houseServiceId"));
                    //封装订单商品数据
                    ItemJSON itemJSON1 = new ItemJSON();
                    itemJSON1.setItemId(houseServiceId);
                    item = itemMapper.getItemInfoByItemId(houseServiceId);
                    itemJSON1.setItemName(item.getItemName());
                    itemJSON1.setQuentity(1);
                    itemJSON1.setPrice(item.getPrice());
                    itemJSON1.setPicture(item.getPicture());
                    itemJSON1.setItemUnit(item.getItemUnit());
                    typeParams.put("takeConsigneeId", takeConsigneeId);
                    typeParams.put("itemJSON", itemJSON1);
                    break;
                case 4:
                    Integer serviceCycle = Integer.parseInt(request.getParameter("serviceCycle"));
                    Integer baseServiceCount = Integer.parseInt(request.getParameter("baseServiceCount"));
                    Integer workTime = Integer.parseInt(request.getParameter("workTime"));
                    String houseArea = request.getParameter("houseArea");
                    BigDecimal baseServicePrice = new BigDecimal(request.getParameter("baseServicePrice"));
                    String individualServiceJson = request.getParameter("individualServiceJson");
                    typeParams.put("serviceCycle", serviceCycle);
                    typeParams.put("baseServiceCount", baseServiceCount);
                    typeParams.put("workTime", workTime);
                    typeParams.put("houseArea", houseArea);
                    typeParams.put("baseServicePrice", baseServicePrice);
                    //封装定制家政商品数据
                    JSONArray jsonArray = JSONArray.fromObject(individualServiceJson);
                    List<ItemJSON> itemJSONList = new ArrayList<>();
                    for (int i=0;i<jsonArray.size();i++){
                        ItemJSON itemJSON2 = new ItemJSON();
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        itemJSON2.setItemId(Integer.parseInt(jsonObject.get("itemId").toString()));
                        itemJSON2.setQuentity(Integer.parseInt(jsonObject.get("quantity").toString()));
                        //查询商品信息
                        item = itemMapper.getItemInfoByItemId(Integer.parseInt(jsonObject.get("itemId").toString()));
                        itemJSON2.setItemName(item.getItemName());
                        itemJSON2.setPicture(item.getPicture());
                        itemJSON2.setPrice(item.getPrice());
                        itemJSON2.setOrderNumber(orderVo.getOrderNumber());
                        itemJSON2.setItemUnit(item.getItemUnit());
                        itemJSONList.add(itemJSON2);
                    }
                    typeParams.put("itemJSONList",itemJSONList);
                    break;
            }
            orderVo.setOrderType(orderType);
            orderVo.setStatus(Constant.ORDER_STATUS_UNPAY);
            orderVo.setPayStatus(Constant.ORDER_PAYSTATUS_NOPAY);
            orderVo.setOriginalPrice(originalPrice);

            orderVo.setTotalPrice(totalPrice);
        }
        Integer createNum = orderService.createOrder(orderVo,request,typeParams);
        //订单创建成功后处理业务
        if(createNum ==1){
            //更新红包
            userService.updateCoupon(couponIdInt);
            //邀请新用户下单赠送红包
            List<OrderVo> orderVoList=  orderService.findOrderListByUserId(userId);
            if(orderVoList.size()==1){
                //赠送随机红包（1-10元：通用类型）
                UserInfoVo userInfoVo = userService.findUserInfoByUserId(userId);
                if(userInfoVo.getSuperId() !=null) {
                    userService.sendRandomCoupon(userInfoVo.getSuperId(), 4);
                }
            }
        }
        OrderVo orderVo1 = orderService.findOrderByOrderNumber(orderVo.getOrderNumber());

        return new FormerResult("success", 0, "下单成功", orderVo1);
    }

    /**
     * 查询订单(预约服务：简单列表)
     */
    @RequestMapping(value = "/findUserReservationList",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo findUserReservationList(Integer userId,HttpServletRequest request){
        /** 校验必须参数 **/
        if (userId == null || userId == 0) {
            return new ResultVo("success", 1, "参数有误,请检查参数", null);
        }
        List<Reservation> reservationList =reservationMapper.getReservationListByUserId(userId);
        //将预约详细列表 变成 简单列表
        List<ReservationListVo> reservationListVoList = new ArrayList<>();
        for(Reservation reservation : reservationList){
            ReservationListVo reservationListVo = new ReservationListVo();
            reservationListVo.setOrderNumber(reservation.getOrderNumber());
            reservationListVo.setReserVationId(reservation.getReservationId());
            reservationListVo.setStatus(reservation.getStatus());
            reservationListVo.setTotalPrice(reservation.getTotalPrice());
            //转化json 为JSONArray 用于计算
            List<ItemJSON> itemJSONList = itemJSONMapper.getItemJSONByReservationId(reservation.getReservationId());
            Integer count=0;
            for(ItemJSON itemJSON : itemJSONList){
                count+=itemJSON.getQuentity();
            }
            reservationListVo.setCount(count);
            //局部变量
            Integer itemId = null;
            AppItemVo appItemVo = null;
            String tempContextUrl = null;
            StringBuffer url = null;
            switch (reservation.getOrderType()){
                case 1:
                    /** 获取请求地址 **/
                    url = request.getRequestURL();
                    /** 拼接 **/
                    tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
                    /** 图片地址 **/
                    reservationListVo.setPicture(tempContextUrl + itemJSONList.get(0).getPicture());
                    reservationListVo.setUnit("件");
                    reservationListVo.setTotalPrice(reservation.getTotalPrice());
                    break;
                case 2:
                    reservationListVo.setPicture("https://www.51lrj.com/othersImg/huiyuanka-img@3x.png");
                    reservationListVo.setUnit("件");
                    reservationListVo.setTotalPrice(new BigDecimal(0.00).setScale(2));
                    break;
                case 3:
                    /** 获取请求地址 **/
                    url = request.getRequestURL();
                    /** 拼接 **/
                    tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
                    /** 图片地址 **/
                    reservationListVo.setPicture(tempContextUrl + itemJSONList.get(0).getPicture());
                    reservationListVo.setUnit("项");
                    reservationListVo.setTotalPrice(reservation.getTotalPrice());
                    break;
                case 4:
                    reservationListVo.setPicture("https://www.51lrj.com/othersImg/huiyuanka-img@3x.png");
                    reservationListVo.setUnit("次");
                    reservationListVo.setTotalPrice(new BigDecimal(0.00).setScale(2));
                    break;
            }
            reservationListVoList.add(reservationListVo);
        }
        return new ResultVo("SUCCESS", 0, "查询成功", reservationListVoList);
    }

    /**
     * 查询订单(预约服务：详情)
     */
    @RequestMapping(value = "/findUserReservationDetail",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult findUserReservationDetail(Integer reservationId,HttpServletRequest request){
        /** 校验必须参数 **/
        if (reservationId == null || reservationId == 0) {
            return new FormerResult("success", 1, "参数有误,请检查参数", null);
        }
        Reservation reservation = reservationMapper.getReservationByReservationId(reservationId);
        //拼接原价
        OrderVo orderVo = orderMapper.getOrderByOrderNumber(reservation.getOrderNumber());
        reservation.setOriginalPrice(orderVo.getOriginalPrice());
        //转化json 为JSONArray 用于计算
        List<ItemJSON> itemJSONList = itemJSONMapper.getItemJSONByReservationId(reservation.getReservationId());
        if(reservation.getOrderType()==1 || reservation.getOrderType()==2){
            //拼接商品信息
            for(ItemJSON itemJSON : itemJSONList){
                /** 获取请求地址 **/
                StringBuffer url = request.getRequestURL();
                /** 拼接 **/
                String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
                /** 拼接可访问图片地址 **/
                itemJSON.setPicture(tempContextUrl+itemJSON.getPicture());
            }
            //清空json,换为jsonArray
            reservation.setItemJSONList(itemJSONList);
        }else {
            //拼接商品信息
            for(ItemJSON itemJSON : itemJSONList){
                /** 获取请求地址 **/
                StringBuffer url = request.getRequestURL();
                /** 拼接 **/
                String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
                /** 拼接可访问图片地址 **/
                itemJSON.setPicture(tempContextUrl+itemJSON.getPicture());
            }
            //清空json,换为jsonArray
            reservation.setItemJSONList(itemJSONList);
        }

        return new FormerResult("SUCCESS", 0, "查询成功", reservation);
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
                //月卡剩余可使用商品列表
                List<ItemJSON> userMonthCardItemList = itemJSONMapper.getItemJSONByOrderNumber(monthCardVo.getOrderNumber());
                monthCardVo.setUserMonthCardItemJSONArray(userMonthCardItemList);
                return new FormerResult("SUCCESS",0,"查询成功！",monthCardVo);
            }
        }else if(type==2){
            List<Order_monthCardVo> monthCardOrderList = orderMapper.getMonthCatdOrderListByUserId(userId);
            if(monthCardOrderList == null || monthCardOrderList.equals("")){
                return new FormerResult("SUCCESS", 0, "查询完成", monthCardOrderList);
            }else {
                //拼接月卡名字
                for (Order_monthCardVo monthCardOrderVo : monthCardOrderList) {
                    MonthCard monthCard = monthCardMapper.getMonthCardById(monthCardOrderVo.getMonthCardId());
                    monthCardOrderVo.setMonthCardName(monthCard.getName());
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
                return new FormerResult("SUCCESS", 0, "查询完成", null);
            }else {
                //定制家政剩余可使用商品列表
                List<ItemJSON> customHouseServiceItemList = itemJSONMapper.getItemJSONByOrderNumber(customHouseServiceVo.getOrderNumber());
                customHouseServiceVo.setIndividualServiceJSONList(customHouseServiceItemList);
                return new FormerResult("SUCCESS",0,"查询成功！",customHouseServiceVo);
            }
        }else if(type==2){
            List<Order_custom_houseServiceVo> customHouseServiceOrderList = orderMapper.getCustomHouseServiceOrderListByUserId(userId);
            if(customHouseServiceOrderList == null || customHouseServiceOrderList.equals("")){
                return new FormerResult("SUCCESS", 0, "查询完成", customHouseServiceOrderList);
            }else {
                return new FormerResult("SUCCESS", 0, "查询完成", customHouseServiceOrderList);
            }
        }
       return null;
    }

    /**
     * 查询用户订单（order）
     */
    @RequestMapping(value = "/getUserOrderList",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getUserOrderList(Integer userId,Integer status){
        //效验必须参数
        if (userId == null || userId==0) {
            return new ResultVo("SUCCESS", 1, "参数有误", null);
        }
        List<OrderVo> orderVoList =null;

        if(status !=null && status!=0){
            orderVoList = orderService.findOrderListByUserIdAndStatus(userId,status);
        }else {
            orderVoList = orderService.findOrderListByUserId(userId);
        }
        return new ResultVo("SUCCESS",0,"查询完成",orderVoList);
    }

    /**
     * 查询是否具有分享订单资格
     */
    @RequestMapping(value = "/isCanShare",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult isCanShare(HttpServletRequest request){
        String orderNumber = request.getParameter("orderNumber");
        OrderVo orderVo = orderService.findOrderByOrderNumber(orderNumber);
        Map<String, Object> params = new HashMap<>();
        if (orderVo.getOrderType() ==1){
            if(orderVo.getShareOrderNumber() !="" && orderVo.getShareOrderNumber() !=null){
                return new FormerResult("SUCCESS", 1, "不可以分享", null);
            }else {
                params.put("orderNumber", orderNumber);
                params.put("activityId", orderVo.getActivity());
                return new FormerResult("SUCCESS", 0, "可以分享", params);
            }
        }else {
            return new FormerResult("SUCCESS", 1, "家政类型订单不可分享", null);
        }

    }

    /**
     * 通过订单号查询 用户购买的商品列表
     */
    @RequestMapping(value = "/getItemJSONByOrderNumber",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getItemJSONByOrderNumber(HttpServletRequest request){
        String orderNumber = request.getParameter("orderNumber");
        /**
         * 效验必须参数
         */
        if(orderNumber == null){
            return new ResultVo("SUCCESS", 1, "参数有误！", null);
        }
        List<ItemJSON> itemJSONList = itemJSONMapper.getItemJSONByOrderNumberFromItemJSONOnly(orderNumber);
        return new ResultVo("SUCCESS", 0, "查询完成！", itemJSONList);
    }
}
