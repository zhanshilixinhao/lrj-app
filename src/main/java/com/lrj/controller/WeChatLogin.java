package com.lrj.controller;
import	java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.baomidou.mybatisplus.extension.api.ApiResult;
import com.lrj.VO.FormerResult;
import com.lrj.VO.ReturnResult;
import com.lrj.VO.WxUserInfo;
import com.lrj.mapper.WxUserInfoMapper;
import com.lrj.pojo.User;
import com.lrj.service.IWeChatLoginService;
import com.lrj.util.AppleUitl;
import com.lrj.util.CommonUtil;
import com.lrj.util.HttpClientTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.lrj.VO.FormerResult.Fail_CODE;
import static com.lrj.VO.FormerResult.SUCCESS_CODE;

/**
 * @author Lxh
 * 微信第三方登录功能
 * @date 2020/4/14 10:28
 */
@RestController
public class WeChatLogin {
    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.secret}")
    private String secret;
    @Resource
    private IWeChatLoginService weChatLoginService;
    @Resource
    private WxUserInfoMapper wxUserInfoMapper;
    @RequestMapping("/toGetAccessToken")
    public FormerResult getAccessToken(HttpServletRequest request){
        FormerResult formerResult = new FormerResult();
        HashMap<String, String> parameter = new HashMap<String,String>();
        String code = request.getParameter("code");
        if (appid==null||secret==null||code == null) {
            return CommonUtil.FAIL(formerResult,"参数异常",null);
        }
        parameter.put("appid",appid);
        parameter.put("secret",secret);
        parameter.put("code",code);
        parameter.put("grant_type","authorization_code");
        String result = HttpClientTool.doGet("https://api.weixin.qq.com/sns/oauth2/access_token", parameter);
        System.out.println(result);
        //设置反序列化下划线转驼峰命名法
        ParserConfig.getGlobalInstance().propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

        ReturnResult returnResult = JSON.parseObject(result, ReturnResult.class);
        if (returnResult.getErrCode()!=null||returnResult.getErrMsg()!=null) {
            return CommonUtil.FAIL(formerResult,returnResult.getErrMsg(),null);
        }
        //检验授权凭证（access_token）是否有效
        String errMsg = inspectionAuthorization(returnResult.getAccessToken(),returnResult.getOpenId());
        if (!"ok".equals(errMsg)) {
            //刷新或续期 access_token 使用
            ReturnResult  newReturnResult = refreshAccessToken(appid,returnResult.getRefreshToken());
            returnResult.setAccessToken(newReturnResult.getAccessToken()).setRefreshToken(newReturnResult.getRefreshToken()).setScope(newReturnResult.getScope()).setOpenId(newReturnResult.getOpenId()).setExpiresIn(newReturnResult.getExpiresIn());
        }

        String userInfo = getUserInfo(returnResult.getAccessToken(),returnResult.getOpenId());
        System.out.println(userInfo);
        ParserConfig.getGlobalInstance().propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        WxUserInfo wxUserInfo = JSON.parseObject(userInfo, WxUserInfo.class);
        wxUserInfoMapper.insertSelective(wxUserInfo);
        System.out.println(wxUserInfo.toString());
        User user = weChatLoginService.findUserByUnionId(wxUserInfo.getUnionId());
        if (user==null) {
            //return CommonUtil.FAIL(formerResult,"用户登录成功!",wxUserInfo);
            return new FormerResult().setErrorCode(2).setErrorTip("请绑定手机号码").setData(userInfo);
        }
        return CommonUtil.SUCCESS(formerResult,"用户登录成功!",user.getAppUserId());
    }
    /*微信登录获取验证码*/
    @RequestMapping("/toGetCaptcha")
    public FormerResult getCaptcha(String userPhone){
        if (userPhone==null) {
            return CommonUtil.FAIL(new FormerResult(),"参数不能为空!",null);
        }
        return weChatLoginService.getCaptcha(userPhone);
    }
    //绑定用户电话号码
    @RequestMapping("/toBindPhoneNumber")
    public FormerResult bindPhoneNumber(String jsonWxUserInfo,String userPhone,String verificationCode,Byte age){
        ParserConfig.getGlobalInstance().propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        WxUserInfo wxUserInfo = JSON.parseObject(jsonWxUserInfo, WxUserInfo.class);
        if (wxUserInfo.getUnionId()==null||userPhone==null) {
            return CommonUtil.FAIL(new FormerResult(),"参数异常",null);
        }
        return weChatLoginService.bindPhoneNumber(wxUserInfo,userPhone,verificationCode,age);
    }

    private String getUserInfo(String accessToken, String openId) {
        HashMap<String, String> parameter = new HashMap<String,String>();
        parameter.put("access_token",accessToken);
        parameter.put("openid",openId);
        return HttpClientTool.doGet("https://api.weixin.qq.com/sns/userinfo", parameter);
    }

    private ReturnResult refreshAccessToken(String appid, String refreshToken) {
        HashMap<String, String> parameter = new HashMap<String,String>();
        parameter.put("appid",appid);
        parameter.put("refresh_token",refreshToken);
        parameter.put("grant_type","refresh_token");
        String result = HttpClientTool.doGet("https://api.weixin.qq.com/sns/oauth2/refresh_token", parameter);
        ParserConfig.getGlobalInstance().propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        return JSON.parseObject(result, ReturnResult.class);
    }

    private String inspectionAuthorization(String accessToken, String openId) {
        HashMap<String, String> parameter = new HashMap<String,String>();
        parameter.put("access_token",accessToken);
        parameter.put("openid",openId);
        String result = HttpClientTool.doGet("https://api.weixin.qq.com/sns/auth", parameter);
        ParserConfig.getGlobalInstance().propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        ReturnResult returnResult = JSON.parseObject(result, ReturnResult.class);
        if (returnResult.getErrCode()==null||returnResult.getErrMsg()==null) {
            return "参数不正确";
        }
        return returnResult.getErrMsg();
    }

    /**
     * @Description: IOS授权登录
     * @Author: LxH
     * @Date: 2020/8/26 16:33
     */
    @RequestMapping("appleLogin")
    @Transactional
    public FormerResult appleLogin(String identityToken){
        try {
            Map<String, String> map = new HashMap<>();
            //验证identityToken
            if(!AppleUitl.verify(identityToken)){
                return new FormerResult("授权验证失败",Fail_CODE,null,null);
            }
            //对identityToken解码
            JSONObject json = AppleUitl.parserIdentityToken(identityToken);
            if(json == null){
                return new FormerResult("授权验证失败",Fail_CODE,"json为空",null);
            }
            String token = json.toString();
            System.out.println(token);
            User user = weChatLoginService.findUserByEmail(json.getString("email"));
            if (user==null) {
                return new FormerResult().setErrorCode(2).setErrorTip("请绑定手机号码").setData(token);
            }
            return new FormerResult("SUCCESS",SUCCESS_CODE,null,user);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new FormerResult("系统错误",Fail_CODE,"json为空",null);
        }
    }
    //绑定用户电话号码
    @RequestMapping("/toAppleBindPhoneNumber")
    public FormerResult AppleBindPhoneNumber(String email,String userPhone,String verificationCode,Byte age,HttpServletRequest request){

        if (email==null||userPhone==null) {
            return CommonUtil.FAIL(new FormerResult(),"参数异常",null);
        }
        return weChatLoginService.AppleBindPhoneNumber(email,userPhone,verificationCode,age,request);
    }
}
