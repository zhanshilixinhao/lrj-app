package com.lrj.controller;

import com.lrj.VO.FormerResult;
import com.lrj.VO.OrderVo;
import com.lrj.VO.ResultVo;
import com.lrj.pojo.Activity;
import com.lrj.service.IActivityService;
import com.lrj.service.IOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : cwj
 * @describe : 活动控制层
 * @date : 2020-7-28
 */
@RestController
public class ActivityController {

    @Resource
    private IActivityService activityService;
    @Resource
    private IOrderService orderService;

    /**
     * 查询用户可参与的活动
     */
    @RequestMapping(value = "/getActivityList",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getActivityList(HttpServletRequest request){
        List<Activity> activityList = activityService.getActivityList();
        /** 获取请求地址 **/
        StringBuffer url = request.getRequestURL();
        /** 拼接 **/
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        for(Activity activity: activityList){
            /** 图片地址 **/
            activity.setPhoto(tempContextUrl + activity.getPhoto());
        }
        return new ResultVo("SUCCESS", 0, "查询成功", activityList);
    }

    /**
     * 查询买一送一活动是否已经被领取
     */
    @RequestMapping(value = "/isActivityByParam",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult isActivityByParam(HttpServletRequest request){
        String shareOrderNumber = request.getParameter("shareOrderNumber");
        OrderVo orderVo = orderService.findOrderByOrderNumber(shareOrderNumber);
        if(orderVo.getIsShare() ==1){
            return new FormerResult("SUCCESS", 0, "该活动已经被领取过了,不可再享受优惠", null);
        }else if(orderVo.getIsShare() ==0){
            return new FormerResult("SUCCESS", 0, "该活动可以正常参与", shareOrderNumber);
        }
        return null;
    }


}
