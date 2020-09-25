package com.lrj.controller;

import com.lrj.VO.*;
import com.lrj.mapper.ReservationMapper;
import com.lrj.mapper.UserMapper;
import com.lrj.pojo.Reservation;
import com.lrj.service.IOrderCommentService;
import com.lrj.service.IStaffService;
import com.lrj.util.DateUtils;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Format;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 评论
 * @date : 2020-4-1
 */
@RestController
public class OrderCommentController {

    @Resource
    private IOrderCommentService commentService;
    @Resource
    private ReservationMapper reservationMapper;
    @Resource
    private IStaffService staffService;
    @Resource
    private UserMapper userMapper;

    /**
     * 获取评论列表数据
     * @return
     */
    @RequestMapping(value = "/listLatestOrderComment",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo listLatestOrderComment(){
        ResultVo resultVo = new ResultVo();
        resultVo.setRequestStatus("success");
        List<OrderCommentVo> orderCommentVoList= commentService.listLatestOrderComment();
        for(OrderCommentVo orderCommentVo : orderCommentVoList){
            UserInfoVo userInfoVo = userMapper.getUserInfoByUserId(orderCommentVo.getUserId());
            orderCommentVo.setNickname(userInfoVo.getNickname());
            orderCommentVo.setHeadPhoto(userInfoVo.getHeadPhoto());
        }
        resultVo.setErrorCode(0);
        resultVo.setErrorTip("查询成功");
        resultVo.setData(orderCommentVoList);
        return resultVo;
    }

    /**
     * 验收服务订单
     */
    @RequestMapping(value = "/addReservationComment",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo addReservationComment(Integer userId,Integer reservationId,String content,Double star,String remark){
        /** 校验必须参数 **/
        if (userId == null || reservationId == null || content==null || star==null || remark==null) {
            return new ResultVo("success", 1, "参数有误,请检查参数", null);
        }
        OrderCommentVo orderCommentVo = new OrderCommentVo();
        orderCommentVo.setCommentContent(content);
        orderCommentVo.setCreateTime(DateUtils.getNowDateTime());
        orderCommentVo.setUserId(userId);
        orderCommentVo.setReservationId(reservationId);
        orderCommentVo.setStar(star);
        orderCommentVo.setRemark(remark);
        Reservation reservation = reservationMapper.getReservationByReservationId(reservationId);
        orderCommentVo.setStaffId(reservation.getGrabOrderIdTake());
        commentService.addReservationComment(orderCommentVo);
        //验收完成后  结算本次服务佣金 存入员工账户
        BigDecimal money = null;
        Double moneyDouble = 0.00;
         if(reservation.getOrderType() == 3 || reservation.getOrderType() ==4){
            List<OrderCommentVo> orderCommentVoList = commentService.getMyReservationComment(reservation.getGrabOrderIdTake());
            double starAll =0;
            for (OrderCommentVo orderComment : orderCommentVoList){
                starAll = starAll + orderComment.getStar();
            }
            double starEnd = starAll/orderCommentVoList.size();
            if(starEnd >= 4.5){
               long serviceTimeLong  = DateUtils.formatStringToDate(reservation.getHouseServiceEndTime()).getTime()-DateUtils.formatStringToDate(reservation.getHouseServiceBeginTime()).getTime();
                Integer serviceTimeInt = Integer.parseInt(String.valueOf(serviceTimeLong / (60*1000)));
                moneyDouble = (serviceTimeInt.doubleValue())*(32.00/60.00);
                StaffInfoVo staffInfoVo = staffService.getStaffInfoByStaffId(reservation.getGrabOrderIdTake());
                //null 转化为0
                if(staffInfoVo.getMoney()==null){
                    money = new BigDecimal(0.00+moneyDouble).setScale(2,BigDecimal.ROUND_HALF_UP);
                }else {
                    money = new BigDecimal(staffInfoVo.getMoney().doubleValue() + moneyDouble).setScale(2,BigDecimal.ROUND_HALF_UP);
                }
                staffInfoVo.setMoney(money);
                if(staffInfoVo.getServiceTime()==null){
                    staffInfoVo.setServiceTime(0);
                }
                staffInfoVo.setServiceTime(staffInfoVo.getServiceTime() + serviceTimeInt);
                staffService.updateStaffInfoAfterEnd(staffInfoVo);
            }else if(starEnd < 4.5){
                long serviceTimeLong  = DateUtils.formatStringToDate(reservation.getHouseServiceEndTime()).getTime()-DateUtils.formatStringToDate(reservation.getHouseServiceBeginTime()).getTime();
                Integer serviceTimeInt = Integer.parseInt(String.valueOf(serviceTimeLong / (60*1000)));
                moneyDouble = serviceTimeInt.doubleValue()*(27.00/60.00);
                StaffInfoVo staffInfoVo = staffService.getStaffInfoByStaffId(reservation.getGrabOrderIdTake());
                //null 转化为0
                if(staffInfoVo.getMoney()==null){
                    money = new BigDecimal(0.00+moneyDouble).setScale(2,BigDecimal.ROUND_HALF_UP);
                }else {
                    money = new BigDecimal(staffInfoVo.getMoney().doubleValue() + moneyDouble).setScale(2,BigDecimal.ROUND_HALF_UP);
                }
                staffInfoVo.setMoney(money);
                if(staffInfoVo.getServiceTime()==null){
                    staffInfoVo.setServiceTime(0);
                }
                staffInfoVo.setServiceTime(staffInfoVo.getServiceTime() + serviceTimeInt);
                staffService.updateStaffInfoAfterEnd(staffInfoVo);
            }
             Map<String, Object> params = new HashMap<>();
             params.put("staffId",reservation.getGrabOrderIdTake());
             params.put("reservationId",reservationId);
             params.put("traceStatus",35);
             reservationMapper.updateReservationTrackingStatus(params);
        }

        return new ResultVo("SUCCESS", 0, "评论成功", null);
    }

}
