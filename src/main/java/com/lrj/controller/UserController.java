package com.lrj.controller;

import com.lrj.VO.*;
import com.lrj.pojo.Balance;
import com.lrj.service.IUserService;

import com.lrj.util.DateUtils;
import com.lrj.util.MessagesUtil;
import com.lrj.util.RandomUtil;
import com.lrj.util.SmsApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

import static com.lrj.util.jiGuangOauthLoginPost.doPostForJpush;

/**
 * @author : cwj
 * @describe : 用户控制层
 * @date : 2020-3-30
 */
@RestController
public class UserController {

    @Resource
    private IUserService userService;

    //定义全局变量 codeMap 存放用户手机验证ma
    Map<String, Object> codeMap = new HashMap<String, Object>();
    /**
     * 一键登录
     */
    /*@RequestMapping(value = "/toOauthLogin",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String,Object> oauthLogin(HttpServletRequest request){
        String token = request.getParameter("token");
        Map<String,Object> resultMap = new HashMap<String,Object>();
        //通过token调用极光接口，获取用户手机号码
        Map<String, Object> requestParams = new HashMap<String, Object>();
        requestParams.put("loginToken", token);
        requestParams.put("exID","swkj001");
        String result = doPostForJpush(token);
        System.out.println(result);
        resultMap.put("result", "success");
        return resultMap;
    }*/

    /**
     * 获取短信验证码
     */
    @RequestMapping(value = "/getVerificationCode",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult getAuthenticationCode(String userPhone){
        /** 校验必须参数 **/
        if (userPhone == null) {
            return new FormerResult("success", 1, "参数有误,请检查参数",null);
        }
        // 生成随机六位验证码
        String verificationCode = RandomUtil.generateOrder(6) + "";
        // 设置发送的内容(内容必须和模板匹配）
        String text = "【懒人家】您的验证码是" + verificationCode + "。如非本人操作，请忽略本短信";
        UserInfoVo userInfoVo = userService.findUserInfoByUserPhone(userPhone);
        // userEntity为空,表示用户不存在.
        if (userInfoVo == null) {
                codeMap.put(userPhone, verificationCode);
                /** 发送短信 **/
            try {
                SmsApi.sendSms(MessagesUtil.getString("apikey"), text, userPhone);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new FormerResult("success",0,"验证码："+verificationCode,null);
        } else {
           /* List<UserInfoVo> userInfoVoList = new ArrayList<UserInfoVo>();
            userInfoVoList.add(userInfoVo);*/
            return new FormerResult("success", 0, "登录成功", userInfoVo);
        }

    }

    /**
     * 被邀请人注册
     */
    @RequestMapping(value = "/inviteRegister",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultVo inviteRegister(String userPhone,String verificationCode,String inviteCode){
        /** 校验必须参数 **/
        if (userPhone == null || verificationCode == null || inviteCode==null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        //注册成功后的用户
        List<UserInfoVo> userInfoVoList=null;
        //检查验证码是否正确
        if(verificationCode.equals(codeMap.get(userPhone))){
            // 检查用户是否存在
            UserInfoVo userInfoVo = userService.findUserInfoByUserPhone(userPhone);
            if (userInfoVo == null) {
              //检查邀请人是否存在
                UserInfoVo visiteUser = userService.findUserByInviteCode(inviteCode);
                if(visiteUser !=null){
                    Integer addCount = userService.addUser(new UserInfoVo().setUserPhone(userPhone).setInvitedCode(inviteCode).setCreateTime(DateUtils.formatDate(new Date())));
                    if(addCount !=1){
                        return new ResultVo("success", 1, "系统错误，请联系客服人员", null);
                    }else {
                        userInfoVoList = (List<UserInfoVo>) userService.findUserInfoByUserPhone(userPhone);
                    }
                }else {
                    return new ResultVo("success", 1, "邀请码错误", null);
                }
            } else {
                return new ResultVo("success", 1, "手机号码已存在", null);
            }
        }else {
            new ResultVo("success", 1, "验证码错误", null);
        }
        //注册成功
        return new ResultVo("success", 0, "注册成功", userInfoVoList);
    }

    /**
     * @功能说明:获取用户当前等级相关信息
     * @param userId
     * @return
     * @throws Exception
     * @返回类型:FormerResult
     * @作者:SAM QZL lxh
     * @版本:1.0
     */
    @RequestMapping(value = "/getUserLevelInfo", method = RequestMethod.POST)
    public FormerResult getUserLevelInfo(Integer userId) throws Exception {
        /** 校验必须参数 **/
        if (userId == null) {
            return new FormerResult("success", 1, "参数有误,请检查参数",null);
        }
        UserLevelVo userLevelVo =userService.findUserLevelInfo(userId);
        return new FormerResult("success",0,"查询成功",userLevelVo);
    }

    /**
     * 获取用户 红包
     */
    @RequestMapping(value = "/getRedPacket",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getUserRedPacket(Integer userId){
        /** 校验必须参数 **/
        if (userId == null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        List<UserCouponVo> userCouponVoList = userService.findUserRedPacket(userId);
        return new ResultVo("success", 0, "查询成功",userCouponVoList);
    }

    /**
     * 获取用户资金信息
     */
    @RequestMapping(value = "/getUserBalanceInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult getUserBalanceInfo(Integer userId){
        /** 校验必须参数 **/
        if (userId == null) {
            return new FormerResult("success", 1, "参数有误,请检查参数",null);
        }
        Balance userBalance = userService.getUserBalanceInfo(userId);
        return new FormerResult("SUCCESS", 0, "查询成功", userBalance);
    }

    /**
     * 获取用户收益流水记录
     */
    @RequestMapping(value = "/getUserRebate",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getUserRebate(Integer userId){
        /** 校验必须参数 **/
        if (userId == null || userId ==0) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        List<UserRebateVo>  userRebateVoList = userService.getUserRebate(userId);
        return new ResultVo("SUCCESS",0,"查询成功！",userRebateVoList);
    }

    /**
     * 获取用户信息
     */
    @RequestMapping(value = "/getUserInfoByUserId",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult getUserInfoByUserId(Integer userId,HttpServletRequest request){
        /** 校验必须参数 **/
        if (userId == null || userId ==0) {
            return new FormerResult("SUCCESS", 1, "参数有误,请检查参数",null);
        }
        UserInfoVo userInfoVo = userService.findUserInfoByUserId(userId);
        if(userInfoVo.getHeadPhoto() ==null || userInfoVo.getHeadPhoto()==""){
            /** 获取请求地址 **/ //request.getRequestURL();
            StringBuffer url = new StringBuffer();
            url = request.getRequestURL();
            /** 拼接 **/
            String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString() + "/";
            userInfoVo.setHeadPhoto("http://thirdwx.qlogo.cn/mmopen/vi_32/PiajxSqBRaEL6b1CZVfzd2nlfv843XZs017uLYAh3ztNibkLHsKa8rAWXu26RticfrSlAHORpWLic9pib0yS1GjSFicA/132");
        }
        return new FormerResult("SUCCESS",0,"查询成功！",userInfoVo);
    }
}
