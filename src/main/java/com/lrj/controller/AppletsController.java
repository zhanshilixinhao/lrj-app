package com.lrj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lrj.VO.FormerResult;
import com.lrj.VO.MiNiUserInfo;
import com.lrj.VO.RawData;
import com.lrj.service.AppletsLogInService;
import com.lrj.service.AppletsPayService;
import com.lrj.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 微信小程序登录
 * @Author: Lxh
 * @Date: 2020/6/9 10:30
 */
@RestController
public class AppletsController {

    @Resource
    private AppletsLogInService appletsLogInService;

    @Resource
    private AppletsPayService appletsPayService;

    private FormerResult result = new FormerResult();

    /**
     * @Description: 微信小程序登录
     * @param code     小程序登录授权码
     * @param userInfo 小程序加密数据
     * @Author: LxH
     * @Date: 2020/6/9 11:01
     */
    @RequestMapping(value = "login")
    public FormerResult login(String code, String userInfo) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(userInfo)) {
            return CommonUtil.FAIL(result, "参数异常", 500);
        }
        RawData data = JSON.parseObject(userInfo, RawData.class);
        return appletsLogInService.login(code, data);
    }

    /**
     * @Description: 微信小程序登录获取验证码
     * @param: phone 接受短息的手机号
     * @Author: LxH
     * @Date: 2020/6/9 15:30
     */
    @RequestMapping(value = "askCode")
    public FormerResult askCode(String phone) throws IOException {
        if (StringUtils.isBlank(phone)) {
            return CommonUtil.FAIL(result, "参数异常", 500);
        }
        return CommonUtil.SUCCESS(result, "发送成功", appletsLogInService.askCode(phone));
    }

    /**
     * @Description: 微信小程序登录绑定手机号
     * @param: phone 接受短息的手机号
     * @param: code  验证码
     * @param: superId
     * @param s1   登录获取的随机数
     * @Author: LxH
     * @Date: 2020/6/9 15:51
     */
    @RequestMapping(value = "bindPhone")
    @ResponseBody
    public FormerResult bindPhone(String phone, String code, String s1,Integer superId,Byte age,Integer merchantId ,String userInfo) {
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(code) || StringUtils.isBlank(s1)) {
            return CommonUtil.FAIL(result, "参数异常", 500);
        }
        MiNiUserInfo miNiUserInfo = JSONObject.parseObject(userInfo, MiNiUserInfo.class);
        return CommonUtil.SUCCESS(result, "绑定成功", appletsLogInService.bindPhone(phone,code,s1,superId,age,merchantId,miNiUserInfo));
    }

    /**
     * @Description: 微信第三方支付
     * @Author: LxH
     * @Date: 2020/6/23 16:05
     */
    @RequestMapping("pay")
    public FormerResult pay(HttpServletRequest request){
        return appletsPayService.pay(request);
    }

    /**
     * @Description: 微信第三方支付回调
     * @Author: LxH
     * @Date: 2020/6/24 15:55
     */
    @RequestMapping("appletsPayNotify")
    public void appletsPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        appletsPayService.appletsPayNotify(request, response);
    }

    /**
     * @Description: 余额充值
     * @Author: LxH
     * @Date: 2020/6/30 17:31
     */
    @RequestMapping("balanceTopUp")
    public FormerResult balanceTopUp(HttpServletRequest request){
        return appletsPayService.balanceTopUp(request);
    }
}
