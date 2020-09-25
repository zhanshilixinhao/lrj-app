package com.lrj.controller;

import com.lrj.VO.FormerResult;
import com.lrj.VO.OrderVo;
import com.lrj.VO.ResultVo;
import com.lrj.mapper.IMerchantMapper;
import com.lrj.pojo.Activity;
import com.lrj.pojo.Merchant;
import com.lrj.pojo.Order;
import com.lrj.service.IActivityService;
import com.lrj.service.IMerchantService;
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
    @Resource
    private IMerchantService merchantService;

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
        if(orderVo.getIsShare() ==null){
            return new FormerResult("SUCCESS", 0, "该活动可以正常参与", shareOrderNumber);
        }else if(orderVo.getIsShare() ==1){
            return new FormerResult("SUCCESS", 0, "该活动已经被领取过了,不可再享受优惠", null);
        }
        return null;
    }

    /**
     * 通过商家ID 查询商家绑定的活动或者订单模板
     */
    @RequestMapping(value = "/merchantCode",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult chooseMerchantIdForContent(HttpServletRequest request){
        Integer merchantId = Integer.parseInt(request.getParameter("merchantId"));
        //效验必须参数
        if (merchantId == null || merchantId==0) {
            return new FormerResult("SUCCESS", 1, "参数有误", null);
        }
        Merchant merchant = merchantService.findMerchantInfoById(merchantId);
        switch (merchant.getType()){
            case 1:
                Activity activity = activityService.getActivityById(merchant.getMerchantActivityId());
                /** 获取请求地址 **/
                StringBuffer url = request.getRequestURL();
                /** 拼接 **/
                String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
                /** 图片地址 **/
                activity.setPhoto(tempContextUrl + activity.getPhoto());
                return new FormerResult("SUCCESS", 0, "判斷完成!", activity);
            case 2:
                return new FormerResult("SUCCESS", 1, "查無此商戶類型！", null);
            case 3:
                return new FormerResult("SUCCESS", 0, "判斷完成！", merchant.getMerchantOrderId());
            default:
                return new FormerResult("SUCCESS", 1, "程序異常！！",null);
        }
    }

    /**
     * 判断该活动是否已经被使用过了
     */
    @RequestMapping(value = "/chooseActivity",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult chooseActivity(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        /**
         * 效验必须参数
         */
        if(userId == null){
            return new FormerResult("SUCCESS", 1, "参数错误！", null);
        }
        List<OrderVo> orderVoList = orderService.findOrderListByUserId(userId);
        if(orderVoList==null){
            return new FormerResult("SUCCESS", 0, "可以参与！", null);
        }else {
            Integer count=0;
            for(OrderVo orderVo : orderVoList){
                if(orderVo.getActivity()==null){
                    continue;
                }else if(orderVo.getActivity()==14){
                    count+=1;
                }
            }
            if(count>0){
                return new FormerResult("SUCCESS", 1, "您已享受过该活动，不可再参与！", null);
            }else {
                return new FormerResult("SUCCESS", 0, "可以参与！", null);
            }
        }
    }
}
