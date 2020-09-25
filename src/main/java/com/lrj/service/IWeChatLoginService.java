package com.lrj.service;

import com.alibaba.fastjson.JSONObject;
import com.lrj.VO.FormerResult;

import com.lrj.VO.WxUserInfo;
import com.lrj.pojo.User;
import org.apache.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Lxh
 * @date 2020/4/14 17:26
 */
public interface IWeChatLoginService {
    void findUserByOpenId(String openId,String userInfo);

    User findUserByUnionId(String unionId);

    FormerResult getCaptcha(String userPhone);

    FormerResult bindPhoneNumber(WxUserInfo wxUserInfo, String userPhone,String verificationCode,Byte age);


    User findUserByEmail(String email);

    FormerResult AppleBindPhoneNumber(String email, String userPhone, String verificationCode, Byte age, HttpServletRequest request);

    /**
     * @Description:
     * @Author: LxH
     * @Date: 2020/8/31 12:54
     */
    FormerResult buildUser(String email, HttpServletRequest request);

    /**
     * @Description: 苹果普通注册
     * @Author: LxH
     * @Date: 2020/9/15 15:59
     */
    FormerResult commonRegister(String userPhone, String verificationCode, String passWord,HttpServletRequest request);
}
