package com.lrj.controller;
import	java.util.HashMap;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;

import com.lrj.VO.FormerResult;
import com.lrj.VO.ReturnResult;
import com.lrj.service.IWeChatLoginService;
import com.lrj.util.CommonUtil;
import com.lrj.util.HttpClientTool;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Lxh
 * 微信第三方登录功能
 *  @date 2020/4/14 10:28
 */
@RestController
public class WeChatLogin {
    @Resource
    private IWeChatLoginService weChatLoginService;
    @RequestMapping("toGetAccessToken")
    public FormerResult getAccessToken(HttpServletRequest request){
        FormerResult formerResult = new FormerResult();
        HashMap<String, String> parameter = new HashMap<>();
        String appid = request.getParameter("appid");
        String secret = request.getParameter("secret");
        String code = request.getParameter("code");
        if (appid==null||secret==null||code == null) {
            return CommonUtil.FAIL(formerResult,"参数异常",null);
        }
        parameter.put("appid",appid);
        parameter.put("secret",secret);
        parameter.put("code",code);
        parameter.put("grant_type","authorization_code");
        String result = HttpClientTool.doGet("https://api.weixin.qq.com/sns/oauth2/access_token", parameter);
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
        weChatLoginService.findUserByOpenId(returnResult.getOpenId(),userInfo);

        return CommonUtil.SUCCESS(formerResult,"用户登录成功",userInfo);
    }

    private String getUserInfo(String accessToken, String openId) {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("access_token",accessToken);
        parameter.put("openid",openId);
        return HttpClientTool.doGet("https://api.weixin.qq.com/sns/userinfo", parameter);
    }

    private ReturnResult refreshAccessToken(String appid, String refreshToken) {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("appid",appid);
        parameter.put("refresh_token",refreshToken);
        parameter.put("grant_type","refresh_token");
        String result = HttpClientTool.doGet("https://api.weixin.qq.com/sns/oauth2/refresh_token", parameter);
        return JSON.parseObject(result, ReturnResult.class);
    }

    private String inspectionAuthorization(String accessToken, String openId) {
        HashMap<String, String> parameter = new HashMap<>();
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
}
