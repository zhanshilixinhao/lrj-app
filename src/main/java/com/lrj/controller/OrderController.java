package com.lrj.controller;

import com.lrj.VO.*;
import com.lrj.constant.Constant;
import com.lrj.mapper.*;
import com.lrj.pojo.MerchantOrder;
import com.lrj.pojo.MonthCard;
import com.lrj.pojo.Reservation;
import com.lrj.service.IOrderService;
import com.lrj.service.IShoppingService;
import com.lrj.service.IUserService;
import com.lrj.util.DateUtils;
import com.lrj.util.MessagesUtil;
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

    /** 创建订单  +增值服务
     * @param request
     * @return
     */
    @RequestMapping(value = "/createOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult createOrder(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        Integer merchantId = Integer.parseInt(request.getParameter("merchantId"));
        Integer couponIdInt = 0;

        OrderVo orderVo = new OrderVo();
        // 生成订单号
        String orderNum = DateUtils.getNewdateBygs("yyyyMMdd") + RandomUtil.generateOrder(6);
        // 处理订单(共同参数)
        orderVo.setOrderNumber(orderNum);
        orderVo.setUserId(userId);
        orderVo.setCreateTime(DateUtils.getNowtime());
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
        //用户正常下单
        }else {
            String couponId = request.getParameter("couponId");
            BigDecimal totalPrice = new BigDecimal(request.getParameter("totalPrice")).setScale(2, RoundingMode.FLOOR); //实际支付金
            BigDecimal originalPrice = new BigDecimal(request.getParameter("originalPrice")); //原价金额
            BigDecimal activityPrice = null; //活动减免金额

            //是否参与活动
            Integer activityId = Integer.parseInt(request.getParameter("activityId"));
            if(activityId !=null && activityId !=0){
                //判断是分享者还是享受者
                String shareOrderNumber = request.getParameter("shareOrderNumber");
                //是享受者
                if(shareOrderNumber !=null){
                    //设置该分享单不可再领取
                    orderService.updateOrderIsShare(1,shareOrderNumber);
                        //判断是新用户还是老用户
                        List<OrderVo> orderVoList=  orderService.findOrderListByUserId(userId);
                        if(orderVoList.size()>0){ //老用户
                            /** 获取购物车商品列表 **/
                            List<ShoppingVo> shoppingVoList = shoppingService.getShoppingDetails(userId);
                            /**获取分享单的商品列表**/
                            Order_washingVo washingVo = orderMapper.getWashingOrderByOrderNumber(shareOrderNumber);
                            String washinDetail = washingVo.getShoppingJson();
                            //比对结果集
                            List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
                            //比对商品减免价格
                            for(ShoppingVo shoppingVo : shoppingVoList){
                                //解析商品json
                                JSONArray wahsingDetailJSONArray = JSONArray.fromObject(washinDetail);
                                for (int i = 0; i < wahsingDetailJSONArray.size(); i++) {
                                    JSONObject d = wahsingDetailJSONArray.getJSONObject(i);
                                    //如果商品相同
                                    if(shoppingVo.getItemId().equals(d.get("itemId"))){
                                        //判断数量
                                        if(shoppingVo.getQuantity().equals(d.get("quantity"))){ //等于
                                            totalPrice = new BigDecimal(totalPrice.doubleValue()-new BigDecimal(d.get("quantity").toString()).doubleValue() * new BigDecimal(d.get("price").toString()).doubleValue());
                                        }else if(shoppingVo.getQuantity() > Integer.parseInt(d.get("quantity").toString())){ //大于
                                            totalPrice = new BigDecimal(totalPrice.doubleValue()-new BigDecimal(d.get("quantity").toString()).doubleValue() * new BigDecimal(d.get("price").toString()).doubleValue());
                                        }else if(shoppingVo.getQuantity() < Integer.parseInt(d.get("quantity").toString())){
                                            totalPrice = new BigDecimal(totalPrice.doubleValue() - new BigDecimal(shoppingVo.getQuantity().toString()).doubleValue() * shoppingVo.getPrice().doubleValue());
                                        }
                                    }else {
                                        continue;
                                    }
                                }
                            }
                        }else if(orderVoList.size()==0){ //新用户
                            BigDecimal shareTotalPrice = new BigDecimal(request.getParameter("shareTotalPrice"));
                            if(totalPrice.doubleValue() <= shareTotalPrice.doubleValue()){
                                totalPrice = new BigDecimal(0);//全免
                            }else {
                                totalPrice = new BigDecimal(totalPrice.doubleValue()-shareTotalPrice.doubleValue()); //减免分享单金额
                            }
                        }
                //是分享者
                }else {
                    totalPrice = totalPrice;
                }
                orderVo.setActivity(activityId);
                orderVo.setActivityPrice(activityPrice);
                orderVo.setShareOrderNumber(shareOrderNumber);
            }else {
                totalPrice = totalPrice;
                orderVo.setActivity(0);
                orderVo.setActivityPrice(new BigDecimal(0));
            }
            //是否使用红包
            if(couponId.equals("") || couponId==null || couponId.equals("0")){
                orderVo.setUserCouponId(couponIdInt);
            }else {
                couponIdInt = Integer.parseInt(couponId);
                orderVo.setUserCouponId(Integer.parseInt(couponId));
            }
            Integer orderType = Integer.parseInt(request.getParameter("orderType"));
            Integer takeConsigneeId = null;
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
                    //封装单项家政数据格式 一致
                    JSONArray array = new JSONArray();
                    JSONObject houseServiceJson = new JSONObject();
                    houseServiceJson.put("itemId", houseServiceId);
                    AppItemVo appItemVo= itemMapper.getItemInfoByItemId(houseServiceId);
                    houseServiceJson.put("itmeName",appItemVo.getItemName());
                    houseServiceJson.put("count", 1);
                    array.add(houseServiceJson);
                    typeParams.put("takeConsigneeId", takeConsigneeId);
                    typeParams.put("houseServiceJson", array.toString());
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
                    typeParams.put("individualServiceJson", individualServiceJson);
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
            JSONArray reservationJsonArray = JSONArray.fromObject(reservation.getReservationJson());
            reservationListVo.setCount(reservationJsonArray.size());
            //局部变量
            Integer itemId = null;
            AppItemVo appItemVo = null;
            String tempContextUrl = null;
            StringBuffer url = null;
            switch (reservation.getOrderType()){
                case 1:
                    itemId = Integer.parseInt(reservationJsonArray.getJSONObject(0).get("itemId").toString());
                    appItemVo= itemMapper.getItemInfoByItemId(itemId);
                    url = new StringBuffer();
                    url.append("http://www.51lrj.com/findUserReservationList");
                    /** 拼接 **/
                    tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString() + "/";
                    /** 获取虚拟目录 **/
                    String directory = MessagesUtil.getString("virtual_directory") + "/";
                    /** 图片地址 **/
                    reservationListVo.setPicture(tempContextUrl + directory + appItemVo.getPicture());
                    reservationListVo.setUnit("件");
                    reservationListVo.setTotalPrice(reservation.getTotalPrice());
                    break;
                case 2:
                    reservationListVo.setPicture("");
                    reservationListVo.setUnit("件");
                    reservationListVo.setTotalPrice(new BigDecimal(0.00).setScale(2));
                    break;
                case 3:
                    itemId = Integer.parseInt(reservationJsonArray.getJSONObject(0).get("itemId").toString());
                    appItemVo = itemMapper.getItemInfoByItemId(itemId);
                    url= new StringBuffer();
                    url.append("http://www.51lrj.com/findUserReservationList");
                    /** 拼接 **/
                    tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString() + "/";
                    /** 获取虚拟目录 **/
                    directory = MessagesUtil.getString("virtual_directory") + "/";
                    /** 图片地址 **/
                    reservationListVo.setPicture(tempContextUrl + directory + appItemVo.getPicture());
                    reservationListVo.setUnit("次");
                    reservationListVo.setTotalPrice(reservation.getTotalPrice());
                    break;
                case 4:
                    reservationListVo.setPicture("");
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
        JSONArray reservationJsonArray = JSONArray.fromObject(reservation.getReservationJson());
        if(reservation.getOrderType()==1 || reservation.getOrderType()==2){
            //拼接商品信息
            for(int i=0;i<reservationJsonArray.size();i++){
                Integer itemId = Integer.parseInt(reservationJsonArray.getJSONObject(i).get("itemId").toString());
                AppItemVo appItemVo = itemMapper.getItemInfoByItemId(itemId);
                /** 获取请求地址 **/
                StringBuffer url = request.getRequestURL();
                /** 拼接 **/
                String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
                /** 拼接可访问图片地址 **/
                reservationJsonArray.getJSONObject(i).element("picture", tempContextUrl  + appItemVo.getPicture());
                reservationJsonArray.getJSONObject(i).element("itemName", appItemVo.getItemName());
                reservationJsonArray.getJSONObject(i).element("defect", "无瑕疵");
                reservationJsonArray.getJSONObject(i).element("price", appItemVo.getPrice());
                reservationJsonArray.getJSONObject(i).element("washingPicture", "http://cwj1.hhhh.com");
            }
            //清空json,换为jsonArray
            reservation.setReservationJson(null);
            reservation.setReservationJSONArray(reservationJsonArray);
        }else {
            //拼接商品信息
            for(int i=0;i<reservationJsonArray.size();i++){
                Integer itemId = Integer.parseInt(reservationJsonArray.getJSONObject(i).get("itemId").toString());
                AppItemVo appItemVo = itemMapper.getItemInfoByItemId(itemId);
                /** 获取请求地址 **/
                StringBuffer url = request.getRequestURL();
                /** 拼接 **/
                String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
                /** 拼接可访问图片地址 **/
                reservationJsonArray.getJSONObject(i).element("picture", tempContextUrl + appItemVo.getPicture());
                reservationJsonArray.getJSONObject(i).element("itemName", appItemVo.getItemName());
            }
            //清空json,换为jsonArray
            reservation.setReservationJson(null);
            reservation.setReservationJSONArray(reservationJsonArray);
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
                //转化json 为JSONArray  供前端使用
                JSONArray userMonthCardItemList = JSONArray.fromObject(monthCardVo.getUserMonthCardItemJson());
                //拼接商品信息
                for(int i=0;i<userMonthCardItemList.size();i++){
                    Integer itemCategoryId = itemMapper.getItemCategoryByItemId(Integer.parseInt(userMonthCardItemList.getJSONObject(i).get("itemId").toString()));
                    ItemCategoryVo itemCategoryVo = itemMapper.getItemCategoryInfoByCategoryId(itemCategoryId);
                    userMonthCardItemList.getJSONObject(i).element("itemCategoryName", itemCategoryVo.getCategoryName());
                }
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
                    //拼接商品信息
                    for(int i=0;i<userMonthCardItemList.size();i++){
                        Integer itemCategoryId = itemMapper.getItemCategoryByItemId(Integer.parseInt(userMonthCardItemList.getJSONObject(i).get("itemId").toString()));
                        ItemCategoryVo itemCategoryVo = itemMapper.getItemCategoryInfoByCategoryId(itemCategoryId);
                        userMonthCardItemList.getJSONObject(i).element("itemCategoryName", itemCategoryVo.getCategoryName());
                    }
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
                return new FormerResult("SUCCESS", 0, "查询完成", null);
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
        if(orderVo.getShareOrderNumber() !="" && orderVo.getShareOrderNumber() !=null){
            return new FormerResult("SUCCESS", 0, "不可以分享", null);
        }else {
            return new FormerResult("SUCCESS", 0, "可以分享", orderNumber);
        }
    }
}
