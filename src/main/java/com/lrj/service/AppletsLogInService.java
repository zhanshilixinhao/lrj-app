package com.lrj.service;

import com.lrj.VO.FormerResult;
import com.lrj.VO.RawData;

import java.io.IOException;

/**
 * @Description:
 * @Author: Lxh
 * @Date: 2020/6/9 10:35
 */
public interface AppletsLogInService {

    /**
     * @Description: 微信小程序登录
     * @param code     小程序登录授权码
     * @Author: LxH
     * @Date: 2020/6/9 11:01
     */
    FormerResult login(String code, RawData data);

    /**
     * @Description: 微信小程序登录获取验证码
     * @param: phone 接受短息的手机号
     * @Author: LxH
     * @Date: 2020/6/9 15:30
     */
    FormerResult askCode(String phone) throws IOException;

    /**
     * @Description: 微信小程序登录绑定手机号
     * @param phone 接受短息的手机号
     * @param code  验证码
     * @param s1   登录获取的随机数
     * @Author: LxH
     * @Date: 2020/6/9 15:51
     */
    FormerResult bindPhone(String phone, String code, String s1,Integer superId,Byte age);
}
