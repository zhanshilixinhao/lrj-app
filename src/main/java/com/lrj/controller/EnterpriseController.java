package com.lrj.controller;

import com.lrj.VO.*;
import com.lrj.mapper.ReservationMapper;
import com.lrj.pojo.Reservation;
import com.lrj.service.IOrderService;
import com.lrj.service.IStaffService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 查询员工可以抢的单
     * @param reservationType
     * @return
     */
    @RequestMapping(value = "/getStaffOrderList",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getWashingOrderList(Integer reservationType){
        /** 校验必须参数 **/
        if (reservationType == null || reservationType == 0) {
            return new ResultVo("success", 1, "参数有误,请检查参数", null);
        }
        List<Reservation> reservationList = null;
        //洗衣（包含单项洗衣和月卡预约）
        if(reservationType == 1){
            reservationList = reservationMapper.getWashingOrderList();
        //家政（包含单项家政和定制家政)
        }else if(reservationType == 2){
            reservationList = reservationMapper.getHouseServiceOrderList();
        }
        return new ResultVo("SUCCESS", 0, "查询成功",reservationList);
    }

    /**
     * 抢单
     * @param staffId
     * @param orderNumber
     * @param request
     * @return
     */
    @RequestMapping(value = "/robOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public synchronized FormerResult robOrder(Integer staffId, String orderNumber, HttpServletRequest request){
        /** 校验必须参数 **/
        if (staffId == null || orderNumber == null) {
            return new FormerResult("SUCCESS", 1, "参数有误,请检查参数",null);
        }
        // 查杀该订单是否被抢
        if(orderService.lockOrderDetailIsLock(orderNumber,staffId)){
            return new FormerResult("SUCCESS", 0, "抢单成功", null);
        }else {
            return new FormerResult("SUCCESS", 0, "抢单失败", null);
        }
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
        if(oldPassWord.equals(staffInfoVo)){
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
    public FormerResult updateReservationStatus(Integer staffId,String orderNumber,Integer traceStatus){
        /** 校验必须参数 **/
        if (staffId == null || orderNumber == null || traceStatus == null) {
            return new FormerResult("SUCCESS", 1, "参数有误,请检查参数",null);
        }
        //参数有多重类型，需封装
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("staffId",staffId);
        params.put("orderNumber",orderNumber);
        params.put("traceStatus",traceStatus);
        Integer updateNumber = reservationMapper.updateReservationStatus(params);
        if(updateNumber ==1){
            return new FormerResult("SUCCESS", 0, "修改成功！", null);
        }else {
            return new FormerResult("SUCCESS", 1, "修改失败！", null);
        }
    }
}
