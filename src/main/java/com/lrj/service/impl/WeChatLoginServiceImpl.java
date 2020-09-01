package com.lrj.service.impl;

import com.alibaba.fastjson.JSON;
import com.lrj.VO.FormerResult;
import com.lrj.VO.WeChatUserInfo;
import com.lrj.VO.WxUserInfo;
import com.lrj.mapper.BalanceMapper;
import com.lrj.mapper.UserLevelMapper;
import com.lrj.mapper.UserMapper;
import com.lrj.pojo.Balance;
import com.lrj.pojo.User;
import com.lrj.pojo.UserLevel;
import com.lrj.service.IWeChatLoginService;
import com.lrj.util.CommonUtil;
import com.lrj.util.DateUtils;
import com.lrj.util.JavaSmsApi;
import com.lrj.util.RandomUtil;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Lxh
 * @date 2020/4/14 17:27
 */
@Service
public class WeChatLoginServiceImpl implements IWeChatLoginService {
    @Value("${bind.apikey}")
    private String apiKey;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserLevelMapper userLevelMapper;
    @Resource
    private BalanceMapper balanceMapper;


    @Override
    public void findUserByOpenId(String openId, String userInfo) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openId", openId);
        List<User> users = userMapper.selectByExample(example);
        if (users == null || users.size() == 0) {
            WeChatUserInfo weChatUserInfo = JSON.parseObject(userInfo, WeChatUserInfo.class);
            User user = new User().setNickName(weChatUserInfo.getNickname()).setOpenId(weChatUserInfo.getOpenid()).setActive(1).setIsCheck(1).setHeadPhoto(weChatUserInfo.getHeadimgurl()).setCreateTime(DateUtils.formatDate(new Date()));
            userMapper.insert(user);
        }
    }

    @Override
    public User findUserByUnionId(String unionId) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("unionId", unionId);
        List<User> users = userMapper.selectByExample(example);
        for (User user : users) {
            return user;
        }
        return null;
    }

    @Override
    public FormerResult getCaptcha(String userPhone) {
        User user = new User();
        HashMap<String, Object> map = new HashMap<String,Object>();
        // 生成随机六位验证码
        String verificationCode = RandomUtil.generateOrder(6) + "";
        user.setVerificationCode(verificationCode);
        int i = userMapper.insertSelective(user);
        System.out.println("添加"+i);
        map.put("verificationCode",verificationCode);
        /*Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("verificationCode", verificationCode);
        List<User> users = userMapper.selectByExample(example);
        for (User user1 : users) {
            if (user1!=null) {
                userMapper.updateByPrimaryKeySelective(user);
            }
        }*/
        // 设置发送的内容(内容必须和模板匹配）
        String text = "【懒人家】您的验证码是" + verificationCode + "。如非本人操作，请忽略本短信";
        try {
            String sms = JavaSmsApi.sendSms(apiKey, text, userPhone);
            System.out.println(sms);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return CommonUtil.SUCCESS(new FormerResult(),"验证码获取成功!",map);
    }

    @Override
    @Transactional
    public FormerResult bindPhoneNumber(WxUserInfo wxUserInfo, String userPhone, String verificationCode,Byte age) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("verificationCode", verificationCode);
        List<User> users = userMapper.selectByExample(example);
        if (users==null) {
            return CommonUtil.FAIL(new FormerResult(),"验证码错误!",null);
        }
        Example example1 = new Example(User.class);
        example1.createCriteria().andEqualTo("userPhone", userPhone);
        List<User> users1 = userMapper.selectByExample(example1);
        if (users1.size()!=0) {
            for (User user : users1) {
                return CommonUtil.SUCCESS(new FormerResult(),"用户绑定手机号码成功",user.getAppUserId());
            }
        }else {
            for (User user : users) {
                user.setNickName(wxUserInfo.getNickName()).setUnionId(wxUserInfo.getUnionId()).setActive(1).setIsCheck(1).setUserPhone(userPhone).setCreateTime(DateUtils.formatDate(new Date())).setVerificationCode(verificationCode).setAppUserId(user.getAppUserId()).setAge(age)
                        .setHeadPhoto(wxUserInfo.getHeadImgUrl());
                try {
                    int i = userMapper.updateByPrimaryKeySelective(user);
                    System.out.println("更新"+i);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    //return CommonUtil.SUCCESS(new FormerResult(),"统一电话号码只能绑定一次",null);
                    FormerResult formerResult = new FormerResult();
                    formerResult.setErrorCode(1).setErrorTip("统一电话号码只能绑定一次").setData(null);
                    return formerResult;
                }
                UserLevel userLevel = new UserLevel();
                int num =0;
                userLevel.setUserId(user.getAppUserId()).setLevelId(1).setInviteNum(num);
                int insert = userLevelMapper.insert(userLevel);
                System.out.println("用户等级"+insert);
                Balance balance = new Balance();
                balance.setUserId(user.getAppUserId()).setBalance(new BigDecimal("0.00")).
                        setCreateTime(DateUtils.formatDate(new Date()));
                balanceMapper.insertSelective(balance);
                return CommonUtil.SUCCESS(new FormerResult(),"用户绑定手机号码成功",user.getAppUserId());
            }
        }
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("email", email);
        List<User> users = userMapper.selectByExample(example);
        for (User user : users) {
            return user;
        }
        return null;
    }

    @Override
    public FormerResult AppleBindPhoneNumber(String email, String userPhone, String verificationCode, Byte age, HttpServletRequest request) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("verificationCode", verificationCode);
        List<User> users = userMapper.selectByExample(example);
        if (users==null) {
            return CommonUtil.FAIL(new FormerResult(),"验证码错误!",null);
        }
        Example example1 = new Example(User.class);
        example1.createCriteria().andEqualTo("userPhone", userPhone);
        List<User> users1 = userMapper.selectByExample(example1);
        if (users1.size()!=0) {
            for (User user : users1) {
                return CommonUtil.SUCCESS(new FormerResult(),"用户绑定手机号码成功",user.getAppUserId());
            }
        }else {
            for (User user : users) {
                user.setNickName("懒人家Apple用户").setActive(1).setIsCheck(1).setUserPhone(userPhone).setCreateTime(DateUtils.formatDate(new Date())).setVerificationCode(verificationCode).setAppUserId(user.getAppUserId()).setAge(age);
                StringBuffer url = request.getRequestURL();
                /** 拼接 **/
                String text = "/userHeadPhotos/touxiang.png";
                String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
                user.setHeadPhoto(tempContextUrl + text).setEmail(email);
                try {
                    int i = userMapper.updateByPrimaryKeySelective(user);
                    System.out.println("更新"+i);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    //return CommonUtil.SUCCESS(new FormerResult(),"统一电话号码只能绑定一次",null);
                    FormerResult formerResult = new FormerResult();
                    formerResult.setErrorCode(1).setErrorTip("统一电话号码只能绑定一次").setData(null);
                    return formerResult;
                }
                UserLevel userLevel = new UserLevel();
                int num =0;
                userLevel.setUserId(user.getAppUserId()).setLevelId(1).setInviteNum(num);
                int insert = userLevelMapper.insert(userLevel);
                System.out.println("用户等级"+insert);
                Balance balance = new Balance();
                balance.setUserId(user.getAppUserId()).setBalance(new BigDecimal("0.00")).
                        setCreateTime(DateUtils.formatDate(new Date()));
                balanceMapper.insertSelective(balance);
                return CommonUtil.SUCCESS(new FormerResult(),"用户绑定手机号码成功",user.getAppUserId());
            }
        }
        return null;
    }

    /**
     * @param: email
     * @Description:
     * @Author: LxH
     * @Date: 2020/8/31 12:54
     */
    @Override
    public FormerResult buildUser(String email, HttpServletRequest request) {
        User user = new User();
        user.setNickName("懒人家Apple用户").setActive(1).setIsCheck(1).setCreateTime(DateUtils.formatDate(new Date()));
        StringBuffer url = request.getRequestURL();
        /** 拼接 **/
        String text = "/headPhoto/avatar.png";
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        user.setHeadPhoto(tempContextUrl + text).setEmail(email);
        userMapper.insertSelective(user);
        UserLevel userLevel = new UserLevel();
        int num =0;
        userLevel.setUserId(user.getAppUserId()).setLevelId(1).setInviteNum(num);
        int insert = userLevelMapper.insert(userLevel);
        System.out.println("用户等级"+insert);
        Balance balance = new Balance();
        balance.setUserId(user.getAppUserId()).setBalance(new BigDecimal("0.00")).
                setCreateTime(DateUtils.formatDate(new Date()));
        balanceMapper.insertSelective(balance);
        return new FormerResult("success",0,null,user.getAppUserId());
    }


}

