package com.lrj.controller;

import com.lrj.VO.FormerResult;
import com.lrj.VO.OrderCommentVo;
import com.lrj.VO.ResultVo;
import com.lrj.VO.StaffInfoVo;
import com.lrj.mapper.ReservationMapper;
import com.lrj.pojo.Reservation;
import com.lrj.service.IOrderCommentService;
import com.lrj.service.IStaffService;
import com.lrj.util.DateUtils;
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

    /**
     * 获取评论列表数据
     * @return
     */
    @RequestMapping(value = "/listLatestOrderComment",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo listLatestOrderComment(){
        ResultVo resultVo = new ResultVo();
        resultVo.setRequestStatus("success");
        List<OrderCommentVo> orderCommentVoList= commentService.listLatestOrderComment();
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
        commentService.addReservationComment(orderCommentVo);
        //验收完成后  结算本次服务佣金 存入员工账户
        Reservation reservation = reservationMapper.getReservationByReservationId(reservationId);
        //洗衣单
        BigDecimal money = null;
        Double moneyDouble = 0.00;
        /*if(reservation.getOrderType()==1 || reservation.getOrderType()==2){
            //送件单
            if(reservation.getGrabOrderIdSend() !=null && reservation.getGrabOrderIdTake() !=null){
                Integer sendDistance = reservation.getSendDistance();
                if(sendDistance < 1000){
                    moneyDouble = 6.00;
                }else if(1000 <= sendDistance && sendDistance <2000){
                    moneyDouble = 6.00+((sendDistance-1000)/1000)*7;
                }else if(2000 <= sendDistance && sendDistance <3000){
                    moneyDouble = 7.00+((sendDistance-2000)/1000)*9;
                }else if(3000 <= sendDistance && sendDistance <4000){
                    moneyDouble = 9.00+((sendDistance-3000)/1000)*11;
                }else if(4000 <= sendDistance && sendDistance <5000){
                    moneyDouble =11.00+((sendDistance-4000)/1000)*13;
                }else if(5000 < sendDistance && sendDistance <6000){
                    moneyDouble = 13.00+((sendDistance-5000)/1000)*15;
                }
                StaffInfoVo staffInfoVo = staffService.getStaffInfoByStaffId(reservation.getGrabOrderIdSend());
                money = new BigDecimal(money.doubleValue() + moneyDouble).setScale(2, BigDecimal.ROUND_HALF_UP);
                staffInfoVo.setMoney(money);
                staffInfoVo.setServiceCount(staffInfoVo.getServiceCount() + 1);
                staffService.updateStaffInfoAfterEnd(staffInfoVo);
            //取件单
            }else if(reservation.getGrabOrderIdTake() !=null && reservation.getGrabOrderIdSend() ==null){
                Integer takeDistrance = reservation.getTakeDistance();
                if(takeDistrance < 1000){
                    moneyDouble = 6.00;
                }else if(1000 <= takeDistrance && takeDistrance <2000){
                    moneyDouble = 6.00+((takeDistrance-1000)/1000)*7;
                }else if(2000 <= takeDistrance && takeDistrance <3000){
                    moneyDouble = 7.00+((takeDistrance-2000)/1000)*9;
                }else if(3000 <= takeDistrance && takeDistrance <4000){
                    moneyDouble = 9.00+((takeDistrance-3000)/1000)*11;
                }else if(4000 <= takeDistrance && takeDistrance <5000){
                    moneyDouble =11.00+((takeDistrance-4000)/1000)*13;
                }else if(5000 < takeDistrance && takeDistrance <6000){
                    moneyDouble = 13.00+((takeDistrance-5000)/1000)*15;
                }
                StaffInfoVo staffInfoVo = staffService.getStaffInfoByStaffId(reservation.getGrabOrderIdTake());
                money = new BigDecimal(money.doubleValue() + moneyDouble).setScale(2, BigDecimal.ROUND_HALF_UP);
                staffInfoVo.setMoney(money);
                staffInfoVo.setServiceCount(staffInfoVo.getServiceCount() + 1);
                staffService.updateStaffInfoAfterEnd(staffInfoVo);
            }
        //家政服务单
        }else*/ if(reservation.getOrderType() == 3 || reservation.getOrderType() ==4){
            List<OrderCommentVo> orderCommentVoList = commentService.getMyReservationComment(reservation.getGrabOrderIdTake());
            double starAll =0;
            for (OrderCommentVo orderComment : orderCommentVoList){
                starAll = starAll + orderComment.getStar();
            }
            double starEnd = starAll/orderCommentVoList.size();
            if(starEnd >= 4.5){
               long serviceTimeLong  = DateUtils.formatStringToDate(reservation.getHouseServiceEndTime()).getTime()-DateUtils.formatStringToDate(reservation.getHouseServiceBeginTime()).getTime();
                Integer serviceTimeInt = Integer.parseInt(String.valueOf(serviceTimeLong / (60*1000)));
                money = new BigDecimal(serviceTimeInt*(32/60));
                StaffInfoVo staffInfoVo = staffService.getStaffInfoByStaffId(reservation.getGrabOrderIdTake());
                staffInfoVo.setMoney(money);
                staffInfoVo.setServiceTime(staffInfoVo.getServiceTime() + serviceTimeInt);
                staffService.updateStaffInfoAfterEnd(staffInfoVo);
            }else if(starEnd < 4.5){
                long serviceTimeLong  = DateUtils.formatStringToDate(reservation.getHouseServiceEndTime()).getTime()-DateUtils.formatStringToDate(reservation.getHouseServiceBeginTime()).getTime();
                Integer serviceTimeInt = Integer.parseInt(String.valueOf(serviceTimeLong / (60*1000)));
                money = new BigDecimal(serviceTimeInt*(27/60));
                StaffInfoVo staffInfoVo = staffService.getStaffInfoByStaffId(reservation.getGrabOrderIdTake());
                staffInfoVo.setMoney(money);
                staffInfoVo.setServiceTime(staffInfoVo.getServiceTime() + serviceTimeInt);
                staffService.updateStaffInfoAfterEnd(staffInfoVo);
            }
        }

        return new ResultVo("SUCCESS", 0, "评论成功", null);
    }

}
