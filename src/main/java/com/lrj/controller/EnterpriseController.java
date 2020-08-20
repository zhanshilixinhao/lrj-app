package com.lrj.controller;

import com.lrj.VO.*;
import com.lrj.constant.Constant;
import com.lrj.mapper.ReservationMapper;
import com.lrj.pojo.DistributionStation;
import com.lrj.pojo.Reservation;
import com.lrj.service.*;
import com.lrj.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author : cwj
 * @describe : 企业端（懒人家助手）（抢单系统）
 * @date : 2020-5-6
 */
@RestController
public class EnterpriseController {

    @Resource
    private ReservationMapper reservationMapper;
    @Resource
    private IOrderService orderService;
   @Resource
   private IStaffService staffService;
   @Resource
   private IOrderCommentService orderCommentService;
   @Resource
   private IDistributionStationService distributionStationService;
   @Resource
   private IUserService userService;


    /**
     * 助手端员工注册
     */
    @RequestMapping(value = "/staffRegister",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult staffRegister(HttpServletRequest request){
        String userPhone = request.getParameter("userPhone");
        String userPassWord = request.getParameter("userPassWord");
        Integer staffType = Integer.parseInt(request.getParameter("staffType"));
        String userName = request.getParameter("userName");
        String userAge = request.getParameter("userAge");
        String userAddress = request.getParameter("userAddress");
        /** 校验必须参数 **/
        if (userPhone == null || userPassWord == null || staffType == null || userName == null || userAge == null || userAddress == null) {
            return new FormerResult("success", 1, "参数有误,请检查参数",null);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userPhone", userPhone);
        params.put("userPassWord", userPassWord);
        params.put("staffType", staffType);
        params.put("userName", userName);
        params.put("userAge", userAge);
        params.put("userAddress", userAddress);
        params.put("registerTime", DateUtils.getNowDateTime());
        Integer insertNum = staffService.staffRegister(params);
        StaffInfoVo staffInfoVo = staffService.findStaffInfoByPhone(userPhone);
        if(insertNum == 1){
            return new FormerResult("SUCCESS", 0, "注册成功！", staffInfoVo);
        }else {
            return new FormerResult("SUCCESS", 1, "注册异常，请联系客服！", staffInfoVo);
        }
    }

    /**
     * 员工获取短信验证码
     */
    @RequestMapping(value = "/getStaffVerificationCode",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult getStaffVerificationCode(String staffPhone){
        /** 校验必须参数 **/
        if (staffPhone == null) {
            return new FormerResult("success", 1, "参数有误,请检查参数",null);
        }
        // 生成随机六位验证码
        String verificationCode = RandomUtil.generateOrder(6) + "";
        // 设置发送的内容(内容必须和模板匹配）
        String text = "【懒人家】您的验证码是" + verificationCode + "。如非本人操作，请忽略本短信";
        StaffInfoVo staffInfoVo = staffService.findStaffInfoByPhone(staffPhone);
        // userEntity为空,表示用户不存在.
        if (staffInfoVo == null) {
            /** 发送短信 **/
            try {
                SmsApi.sendSms(MessagesUtil.getString("apikey"), text, staffPhone);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new FormerResult("success",0,"验证码已发送，请查收。。",verificationCode);
        } else {
            return new FormerResult("success", 1, "对不起，该手机号已经被注册！", null);
        }
    }

    /**
     * 员工登录
     * @param loginName
     * @param staffPassword
     * @param req
     * @return
     */
    @RequestMapping(value="/enterpriseUserLogin",method=RequestMethod.POST)
    public  FormerResult enterpriseUserLogin(String loginName,String staffPassword,HttpServletRequest req){

        if (loginName == null || staffPassword == null) {
            return new FormerResult("SUCCESS",1,"参数有误,请检查参数",null);
        }

        Map<String,String> parMap = new HashMap<String, String>();
        parMap.put("loginName", loginName);
        parMap.put("staffPassword", staffPassword);
        StaffInfoVo staffInfoVo = staffService.findStaffInfoByLoginInfo(parMap);

        if (staffInfoVo != null) {
            return new FormerResult("SUCCESS", 0, "登录成功", staffInfoVo);
        } else {
            return new FormerResult("SUCCESS",1,"登录名或密码错误",null);
        }
    }
    /**
     * 查询员工可以抢的单
     * @return
     */
    @RequestMapping(value = "/getStaffOrderList",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getWashingOrderList(Integer staffType,HttpServletRequest request){
        /** 校验必须参数 **/
        if (staffType == null || staffType == 0) {
            return new ResultVo("success", 1, "参数有误,请检查参数", null);
        }
        List<Reservation> reservationList = null;
        //计算两个坐标之间的实际距离
        String key = "81b5d90e4a546330f9e0672877299bc0"; //此key 为高德地图提供，使用API接口必传的key
        String url = "https://restapi.amap.com/v3/distance"; //距离测量API url
        switch (staffType){
            case 1:
                //洗衣订单（所有）
                reservationList = reservationMapper.getWashingOrderList();
                for(Reservation reservation : reservationList){
                    Map<String, Integer> distribationMap = new HashMap<>();
                    //(初始地 坐标)
                    String origins = reservation.getLongitude() + "," + reservation.getLatitude();
                    //比对挑选最短距离的配送站 存储起来，防止因刷新导致高德地图API访问次数过大，超过了开发者访问限制次数
                    List<DistributionStation> distributionStationControllerList = distributionStationService.getDistriButionStationList();
                    for(DistributionStation distributionStation : distributionStationControllerList){
                        CloseableHttpResponse response = null;
                        CloseableHttpClient httpClient = null;
                        //(目的地 坐标)
                        String destination = distributionStation.getLongitude() + "," + distributionStation.getLatitude();
                        try {
                            String responseContent = "";
                            httpClient= HttpClients.createDefault();
                            //创建URLBuilder
                            URIBuilder uriBuilder= new URIBuilder(url);
                            //设置参数
                            uriBuilder.setParameter("origins",origins).setParameter("destination",destination).setParameter("key",key);
                            HttpGet httpGet=new HttpGet(uriBuilder.build());
                            response = httpClient.execute(httpGet);
                            HttpEntity entity = response.getEntity();
                            responseContent = EntityUtils.toString(entity, "UTF-8");
                            JSONObject jsonObject = JSONObject.fromObject(responseContent);
                            //获取距离
                            Integer distance = Integer.parseInt(JSONObject.fromObject(JSONArray.fromObject(jsonObject.get("results")).get(0)).getString("distance"));
                            //装入map  用来做比较
                            distribationMap.put(distributionStation.getDistributionStationId().toString(), distance);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }finally {
                            try {
                                response.close();
                                httpClient.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    //排序得到最小的值
                    List<Map.Entry<String, Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(distribationMap.entrySet());
                    Collections.sort(list_Data, new Comparator<Map.Entry<String, Integer>>() {
                        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                            //升序排列
                            return (o1.getValue() - o2.getValue());
                        }
                    });
                    reservation.setBeeline(list_Data.get(0).getValue());
                    DistributionStation distributionStation = distributionStationService.getDistriButionStationById(list_Data.get(0).getKey());
                    reservation.setDistributionAddress(distributionStation.getDistributionStationAddress());
                    UserInfoVo userInfoVo = userService.findUserInfoByUserId(reservation.getUserId());
                    reservation.setUserName(userInfoVo.getNickname());
                    reservation.setUserPhone(userInfoVo.getUserPhone());
                }
                break;
            case 2:
                break;
            case 3:
                //家政订单（所有)
                reservationList = reservationMapper.getHouseServiceOrderList();
                for(Reservation reservation : reservationList){
                    UserInfoVo userInfoVo = userService.findUserInfoByUserId(reservation.getUserId());
                    reservation.setUserName(userInfoVo.getNickname());
                    reservation.setUserPhone(userInfoVo.getUserPhone());
                }
                break;
            case 4:break;

        }
        return new ResultVo("SUCCESS", 0, "查询成功",reservationList);
    }

    /**
     * 抢单
     * @param staffId
     * @param reservationId
     * @param request
     * @return
     */
    @RequestMapping(value = "/robOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public synchronized FormerResult robOrder(Integer staffId, String reservationId, HttpServletRequest request){
        /** 校验必须参数 **/
        if (staffId == null || reservationId == null) {
            return new FormerResult("SUCCESS", 1, "参数有误,请检查参数",null);
        }
        // 查杀该订单是否被抢
        if(orderService.lockOrderDetailIsLock(reservationId,staffId)){
            return new FormerResult("SUCCESS", 0, "抢单成功", null);
        }else {
            return new FormerResult("SUCCESS", 1, "抢单失败", null);
        }
    }

    /**
     * 根据员工Id和状态查询对应的服务单
     */
    @RequestMapping(value = "/getReservationByStatus",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getReservationByStatus(Integer trackingStatus,Integer staffId) {
        /** 校验必须参数 **/
        if (trackingStatus == null || trackingStatus == 0 || staffId == null || staffId == 0) {
            return new ResultVo("SUCCESS", 1, "参数有误,请检查参数", null);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("staffId", staffId);
        params.put("trackingStatus", trackingStatus);
        List<Reservation> reservationList= null;
        if(trackingStatus == 1){
            reservationList = reservationMapper.getReservationByStatus1(params);
        }else if(trackingStatus == 9){
            reservationList = reservationMapper.getReservationByStatus2(params);
        }else if(trackingStatus == 33){
            reservationList = reservationMapper.getReservationByStatus1(params);
        }
        for(Reservation reservation : reservationList){
            UserInfoVo userInfoVo = userService.findUserInfoByUserId(reservation.getUserId());
            reservation.setUserName(userInfoVo.getNickname());
            reservation.setUserPhone(userInfoVo.getUserPhone());
        }

        return new ResultVo("SUCCESS", 0, "查询成功", reservationList);
    }

    /**
     * 修改密码
     * @param staffId
     * @param oldPassWord
     * @param newPassWord
     * @return
     */
    @RequestMapping(value = "/updateStaffPassWord",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult updateStaffPassWord(Integer staffId,String oldPassWord,String newPassWord){
        /** 校验必须参数 **/
        if (staffId == null || oldPassWord == null || newPassWord == null) {
            return new FormerResult("SUCCESS", 1, "参数有误,请检查参数",null);
        }
        StaffInfoVo staffInfoVo = staffService.getStaffInfoByStaffId(staffId);
        //匹配原密码
        if(oldPassWord.equals(staffInfoVo.getStaffPassword())){
            staffService.updateStaffPassWord(staffId,newPassWord);
            return new FormerResult("SUCCESS", 0, "修改成功", null);
        }else {
            return new FormerResult("SUCCESS", 1, "原密码不正确，请重试", null);
        }
    }

    /**
     * 更改预约订单追踪状态
     */
    @RequestMapping(value = "/updateReservationStatus",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult updateReservationStatus(Integer staffId,String reservationId,Integer traceStatus){
        /** 校验必须参数 **/
        if (staffId == null || reservationId == null || traceStatus == null) {
            return new FormerResult("SUCCESS", 1, "参数有误,请检查参数",null);
        }
        //参数有多重类型，需封装
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("staffId",staffId);
        params.put("reservationId",reservationId);
        params.put("traceStatus",traceStatus);
        //如果为家政服务状态，则记录服务时间
        if(traceStatus == Constant.ORDER_TRANSSTATUS_HOUSESERVICE_BEGIN){
            params.put("houseServiceBeginTime", DateUtils.getNowDateTime());
        }else if(traceStatus == Constant.ORDER_TRANSSTATUS_HOUSESERVICE_END){
            params.put("houseServiceEndTime", DateUtils.getNowDateTime());
        }
        Integer updateNumber = reservationMapper.updateReservationTrackingStatus(params);
        if(updateNumber ==1){
            return new FormerResult("SUCCESS", 0, "修改成功！", null);
        }else {
            return new FormerResult("SUCCESS", 1, "修改失败！", null);
        }
    }

    /**
     * 添加订单备注
     *
     */
    @RequestMapping(value = "/addOrderRemark", method = RequestMethod.POST)
    public FormerResult addOrderItem(HttpServletRequest request) {
        SmsTemplateVo smsTemplateVo = new SmsTemplateVo();
        smsTemplateVo.setCreateTime(DateUtils.getNowTime("yyyy-MM-DD mm:hh:ss"));
        smsTemplateVo.setIsBallingUp(Integer.parseInt(request.getParameter("isBallingUp")));
        smsTemplateVo.setIsCrossColor(Integer.parseInt(request.getParameter("isCrossColor")));
        smsTemplateVo.setIsDye(Integer.parseInt(request.getParameter("isDye")));
        smsTemplateVo.setIsWearOut(Integer.parseInt(request.getParameter("isWearOut")));
        smsTemplateVo.setIsYellowWhite(Integer.parseInt(request.getParameter("isYellowWhite")));
        smsTemplateVo.setOrderNumber(request.getParameter("'orderNumber"));
        smsTemplateVo.setOther(request.getParameter("other"));
        orderService.addOrderRemark(smsTemplateVo);
        return new FormerResult("SUCCESS", 0, "添加成功！", null);
    }

    /**
     * 删除订单备注
     *
     */
    @RequestMapping(value = "/deleteOrderRemark", method = RequestMethod.POST)
    public FormerResult deleteOrderRemark(HttpServletRequest request) {
        String orderNumber = request.getParameter("orderNumber");
        /** 校验必须参数 **/
        if (orderNumber == null || orderNumber.equals("")) {
            return new FormerResult("SUCCESS", 1, "参数有误,请检查参数",null);
        }
        orderService.deleteOrderRemark(orderNumber);
        return new FormerResult("SUCCESS", 0, "删除成功！", null);
    }

    /**
     * 打印
     */
    @RequestMapping(value = "/printInfo", method = RequestMethod.POST)
    public FormerResult printInfo(FormerResult result, HttpServletRequest request) {
        return new FormerResult("SUCCESS", 0, "打印完成！", null);
    }

    /**
     * 家政服务评价
     */
    @RequestMapping(value = "/getMyReservationComment",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getMyReservationComment(HttpServletRequest request,Integer staffId){
        /** 校验必须参数 **/
        if (staffId == null || staffId == 0) {
            return new ResultVo("SUCCESS", 1, "参数有误,请检查参数",null);
        }
        List<OrderCommentVo> orderCommentVoList = orderCommentService.getMyReservationComment(staffId);
        for(OrderCommentVo orderCommentVo : orderCommentVoList){
            UserInfoVo userInfoVo = userService.findUserInfoByUserId(orderCommentVo.getUserId());
            orderCommentVo.setUserId(orderCommentVo.getUserId());
            /** 获取请求地址 **/
            StringBuffer url = request.getRequestURL();
            /** 拼接 **/
            String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
            orderCommentVo.setHeadPhoto(tempContextUrl  + userInfoVo.getHeadPhoto());
            orderCommentVo.setUserPhone(userInfoVo.getUserPhone());
            orderCommentVo.setNickname(userInfoVo.getNickname());
        }
        return new ResultVo("SUCCESS", 0, "查询成功！", orderCommentVoList);
    }

    /**
     * 家政服务时长统计
     */
    @RequestMapping(value = "/getHouseServiceWorkTime",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult getHouseServiceWorkTime(HttpServletRequest request,Integer staffId){
        /** 校验必须参数 **/
        if (staffId == null || staffId == 0) {
            return new FormerResult("SUCCESS", 1, "参数有误,请检查参数",null);
        }
        List<HouseServiceWorkTimeVo> houseServiceWorkTimeVoList = reservationMapper.getHouseServiceWorkTime(staffId);
        return new FormerResult("SUCCESS", 0, "查询成功！", houseServiceWorkTimeVoList);
    }

    /**
     * 查询员工个人中心 信息
     */
    @RequestMapping(value = "/getStaffInfoCentre",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult getStaffInfoCentre(Integer staffId){
        /** 校验必须参数 **/
        if (staffId == null || staffId == 0) {
            return new FormerResult("SUCCESS", 1, "参数有误,请检查参数",null);
        }
        StaffInfoVo staffInfoVo = staffService.getStaffInfoByStaffId(staffId);
        return new FormerResult("SUCCESS", 0, "查询成功！", staffInfoVo);
    }
}
