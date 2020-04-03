package com.lrj.controller;

import com.lrj.VO.FormerResult;
import com.lrj.service.IBalanceService;
import com.lrj.service.IMemberServiceUserRelationService;
import com.lrj.service.IUserLevelVoService;
import com.lrj.util.CommonUtil;
import com.lrj.util.RequestParameterUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lxh
 * @date 2020/4/1 17:37
 */
@RestController
public class AppUserActionContronller {
    @Resource
    private IBalanceService iBalanceService;
    @Resource
    private IMemberServiceUserRelationService iMemberServiceUserRelationService;

    @Resource
    private IUserLevelVoService iUserLevelVoService;
    @RequestMapping(value = "/getUserMoneyInfo", method = RequestMethod.POST)
    public FormerResult getUserMoneyInfo(FormerResult result, HttpServletRequest request){
        try {
            if (RequestParameterUtil.checkRequestParametersIsNull(request,new String[]{"userId"},result)) {
                return result;
            }
            /** 获取参数 **/
            Integer userId = Integer.valueOf(request.getParameter("userId"));
            return iBalanceService.findBalanceByUserId(userId,result,request);
        } catch (Exception e) {
            return CommonUtil.FAIL(result,null,e.getMessage());
        }
    }
    /**
     * @功能说明:获取用户当前等级相关信息
     * @param result
     * @param request
     * @return
     * @throws Exception
     * @返回类型:FormerResult
     * @作者:SAM QZL lxh
     * @版本:1.0
     */
    @RequestMapping(value = "/getUserLevelInfo/v4", method = RequestMethod.POST)
    public FormerResult getUserLevelInfo(FormerResult result, HttpServletRequest request) throws Exception {

        /** 校验必要参数 **/
        if (RequestParameterUtil.checkRequestParametersIsNull(request, new String[] { "userId" }, result)) {
            return result;
        }
        /** 获取参数 **/
        Integer userId = Integer.valueOf(request.getParameter("userId"));
        return iUserLevelVoService.findCurrentUserLevelInfo(userId,result);
    }

    /**
     * @功能说明:获取用户当月剩余额度用于支付。
     * @param result
     *            结果对象
     * @param request
     *            请求对象
     * @return 结果对象
     * @throws Exception
     * @返回类型:FormerResult
     * @方法名称:getResidueLimit
     * @作者:SAM QZL lxh
     * @版本:1.0
     */
    @RequestMapping(value = "/getResidueLimit/v3", method = RequestMethod.POST)
    public FormerResult getResidueLimit(FormerResult result, HttpServletRequest request) throws Exception {

        /** 校验必须字段 **/
        if (RequestParameterUtil.checkRequestParametersIsNull(request, new String[] { "userId" }, result)) {
            return result;
        }
        /** 返回结果对象 **/
        Map<String, Object> res = new HashMap<String, Object>();
        /** 获取参数 **/
        Integer userId = Integer.valueOf(request.getParameter("userId"));

        /** 判断是否已经是会员 **/
        if (!iMemberServiceUserRelationService.judgeTheUserIsMemberService(userId)) {
            /** 额度0 **/
            res.put("residueLimit", 0);
            /** 返回结果 **/
            return CommonUtil.SUCCESS(result, "查询成功！", null);
        }

        /** 查询用户当月额度 **/
        BigDecimal residueLimit = iMemberServiceUserRelationService.findResidueLimitByUserId(userId);
        /** 当月额度 **/
        res.put("residueLimit", residueLimit);
        /** 返回结果 **/
        return CommonUtil.SUCCESS(result, "查询成功！", res);
    }
    /**
     * @功能说明:判断用户是否是会员接口。
     * @param result
     *            结果对象
     * @param request
     *            请求对象
     * @return 结果对象
     * @throws Exception
     * @返回类型:FormerResult
     * @方法名称:judgeUserIsMemberService
     * @作者:SAM QZL lxh
     * @版本:1.0
     */
    @RequestMapping(value = "/judgeUserIsMemberService/v3", method = RequestMethod.POST)
    public FormerResult judgeUserIsMemberService(FormerResult result, HttpServletRequest request) throws Exception {
        /** 校验必须字段 **/
        if (RequestParameterUtil.checkRequestParametersIsNull(request, new String[] { "userId" }, result)) {
            return result;
        }
        /** 获取参数 **/
        Integer userId = Integer.valueOf(request.getParameter("userId"));

        /** 调用判断会员业务方法 **/
        Integer isMemberService = iMemberServiceUserRelationService.judgeTheUserIsMemberService(userId)==true ? 1 : 0;

        /** 结果 **/
        Map<String, Object> res = new HashMap<String, Object>();
        /** 放入map **/
        res.put("isMemberService", isMemberService);
        /** 返回结果 **/
        return CommonUtil.SUCCESS(result, "获取成功!", res);

    }
}
