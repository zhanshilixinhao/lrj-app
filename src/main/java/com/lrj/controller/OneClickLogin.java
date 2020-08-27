package com.lrj.controller;


import com.alibaba.fastjson.JSON;
import com.lrj.VO.FormerResult;
import com.lrj.VO.LoginMess;
import com.lrj.mapper.BalanceMapper;
import com.lrj.mapper.UserLevelMapper;
import com.lrj.mapper.UserMapper;
import com.lrj.pojo.Balance;
import com.lrj.pojo.User;

import com.lrj.pojo.UserLevel;
import com.lrj.service.IUserService;
import com.lrj.util.CommonUtil;

import com.lrj.util.DateUtils;
import com.lrj.util.JiGuangOauthLogin;
import com.lrj.util.jiGuangOauthLoginPost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Lxh
 * @date 2020/4/7 9:36
 */
@RestController

public class OneClickLogin {
    @Resource
    private IUserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserLevelMapper userLevelMapper;
    @Resource
    private BalanceMapper balanceMapper;
    /**
     * 接收极光推送的token
     */
    @Value("${login.prikey}")
    private String prikey;
    @RequestMapping(value = "/toOauthLogin",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult oauthLogin(HttpServletRequest request) {
        FormerResult formerResult = new FormerResult();
        String token = request.getParameter("loginToken");
        //通过token调用极光接口，获取用户手机号码
        LoginMess loginMess = JSON.parseObject(jiGuangOauthLoginPost.doPostForJpush(token), LoginMess.class);
        if (loginMess.getPhone()==null||loginMess.getId()==null) {
            return CommonUtil.FAIL(formerResult,"参数错误!",null);
        }
        try {
            String phoneNum = JiGuangOauthLogin.decrypt(loginMess.getPhone(), prikey);
            List<User> userList = userService.findUserByPhone(phoneNum);
            //如果没有注册过
            if (userList.size()==0||userList==null) {
                User user = new User().setUserPhone(phoneNum).setActive(1).setNickName("懒人家新用户").setIsCheck(1).setCreateTime(DateUtils.formatDate(new Date()));
                /** 获取请求地址 **/
                StringBuffer url = request.getRequestURL();
                /** 拼接 **/
                String text = null;
                String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
                user.setHeadPhoto(tempContextUrl + text);
                int insert = userMapper.insert(user);
                Example example = new Example(User.class);
                example.createCriteria().andEqualTo("userPhone",phoneNum);
                List<User> users = userMapper.selectByExample(example);
                for (User user1 : users) {
                    /**用户等级*/
                    UserLevel userLevel = new UserLevel();
                    userLevel.setUserId(user1.getAppUserId()).setLevelId(1).setInviteNum(0);
                    int userLevelInsert = userLevelMapper.insert(userLevel);
                    System.out.println("用户等级"+userLevelInsert);
                    Balance balance = new Balance();
                    balance.setUserId(user1.getAppUserId()).setBalance(new BigDecimal("0.00")).
                            setCreateTime(DateUtils.formatDate(new Date()));
                    balanceMapper.insertSelective(balance);
                }
                if (insert==0) {
                    return CommonUtil.FAIL(formerResult,"用户添加失败!",null);
                }else {
                    List<User> userListNew = userService.findUserByPhone(phoneNum);
                    for (User user1 : userListNew) {
                        return CommonUtil.SUCCESS(formerResult,"用户登录成功!",user1);
                    }
                }
             //已经注册过了
            }else {
                for (User user : userList) {
                    return CommonUtil.SUCCESS(formerResult,"用户登录成功!",user);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return null;
    }
}
