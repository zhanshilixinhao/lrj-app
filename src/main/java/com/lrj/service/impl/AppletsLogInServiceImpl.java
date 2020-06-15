package com.lrj.service.impl;

import com.lrj.VO.RawData;
import com.lrj.VO.WXResult;
import com.lrj.common.ErrorCode;
import com.lrj.exception.ServiceException;
import com.lrj.mapper.ThirdAccMapper;
import com.lrj.mapper.UserLevelMapper;
import com.lrj.mapper.UserMapper;
import com.lrj.pojo.ThirdAcc;
import com.lrj.pojo.User;
import com.lrj.pojo.UserLevel;
import com.lrj.service.AppletsLogInService;
import com.lrj.util.*;
import com.mysql.cj.Messages;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.lrj.pojo.ThirdAcc.COLUMN_THIRDACC_OPEN_ID;
import static com.lrj.pojo.ThirdAcc.COLUMN_THIRDACC_PHONE;
import static com.lrj.pojo.User.COLUMN_USER_PHONE;

/**
 * @Description:
 * @Author: Lxh
 * @Date: 2020/6/9 10:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppletsLogInServiceImpl implements AppletsLogInService {

    @Value("${bind.apikey}")
    private String apiKey;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserLevelMapper userLevelMapper;

    @Resource
    private ThirdAccMapper thirdAccMapper;

    private static final Logger log = LoggerFactory.getLogger(AppletsLogInServiceImpl.class);

    /**缓存*/
    private static final UserCache userCache = new UserCache(3, TimeUnit.MINUTES);

    /**
     * @param: code 小程序登录授权码
     * @param: data
     * @Description: 微信小程序登录
     * @Author: LxH
     * @Date: 2020/6/9 11:01
     */
    @Override
    public Object login(String code, RawData userInfo) {
        // 获取微信小程序的openid
        WXResult result = WXCodeApi.getSession(3, code);
        // 如果获取失败
        if (result.getErrcode() != 0) {
            throw new ServiceException(result.getErrmsg(), result.getErrcode());
        }
        // 获取成功
        // 1.判断该openid是否存在
        Example example = new Example(ThirdAcc.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isBlank(result.getOpenid())) {
            criteria.andEqualTo(COLUMN_THIRDACC_OPEN_ID,result.getOpenid());
        }
        List<ThirdAcc> thirdAccs = thirdAccMapper.selectByExample(example);
        for (ThirdAcc thirdAcc : thirdAccs) {
            // 1.1如果账号不存在需要绑定手机号
            if (thirdAcc == null) {
                // 产生一个随机数
                String key = UUID.randomUUID().toString();
                userInfo.setOpenid(result.getOpenid());
                userCache.put(key,userInfo.getOpenid());
                Map<String, String> map = new HashMap<>();
                map.put("s1", key);
                throw new ServiceException(ErrorCode.NEED_BIND_PHONE).data(map);
                // 如果账号存在，取出用户信息
            }
            Example e = new Example(User.class);
            Example.Criteria c = e.createCriteria();
            if (StringUtils.isBlank(thirdAcc.getPhone())) {
                c.andEqualTo(COLUMN_USER_PHONE,thirdAcc.getPhone());
            }
            List<User> users = userMapper.selectByExample(e);
            for (User user : users) {
                if (user != null && user.getActive() != null && user.getActive() != 1) {
                    throw new ServiceException(ErrorCode.ACC_DISABLED);
                }
                return user;
            }
        }
        return null;
    }

    /**
     * @param: phone
     * @Description: 微信小程序登录获取验证码
     * @param: phone 接受短息的手机号
     * @Author: LxH
     * @Date: 2020/6/9 15:30
     */
    @Override
    public Object askCode(String phone) throws IOException {
        // 判断账号是否存在
        Example example = new Example(ThirdAcc.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isBlank(phone)) {
            criteria.andEqualTo(COLUMN_THIRDACC_PHONE,phone);
        }
        List<ThirdAcc> thirdAccs = thirdAccMapper.selectByExample(example);
        for (ThirdAcc thirdAcc : thirdAccs) {
            // 如果已绑定账号
            if (thirdAcc != null) {
                throw new ServiceException(ErrorCode.BIND_PHONE_EXIST);
            }
            // 生成随机六位验证码
            String verificationCode = RandomUtil.generateOrder(6) + "";
            // 设置发送的内容(内容必须和模板匹配)
            String text = "【懒人家】您的验证码是" + verificationCode + "。如非本人操作，请忽略本短信";
            // 存储验证码
            userCache.put(phone, verificationCode);
            /** 发送短信 **/
            String str = SmsApi.sendSms(apiKey, text, phone);
            return str;
        }
        return null;
    }

    /**
     * @param phone 接受短息的手机号
     * @param code  验证码
     * @param s1    登录获取的随机数
     * @Description: 微信小程序登录绑定手机号
     * @Author: LxH
     * @Date: 2020/6/9 15:51
     */
    @Override
    public Object bindPhone(String phone, String code, String s1,Integer superId,Byte age) {
        // 判断s1是否存在
        String json = userCache.getCache(s1);
        log.info(json);
        if (org.apache.commons.lang.StringUtils.isBlank(json)) {
            throw new ServiceException(ErrorCode.OPENID_DISABLED);
        }
        // RawData wxUserInfo = JSON.parseObject(json, RawData.class);
        // String openid = wxUserInfo.getOpenid();
        String openid = json;

        // 判断验证码
        String currentCode = userCache.getCache(phone);
        if (!org.apache.commons.lang.StringUtils.equals(currentCode, code)) {
            throw new ServiceException(ErrorCode.VERIFY_CODE_ERR);
        }
        // 判断账号是否存在
        Example example = new Example(ThirdAcc.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isBlank(phone)) {
            criteria.andEqualTo(COLUMN_THIRDACC_PHONE,phone);
        }
        List<ThirdAcc> thirdAccs = thirdAccMapper.selectByExample(example);
        for (ThirdAcc thirdAcc : thirdAccs) {
            // 如果已绑定账号
            if (thirdAcc!=null) {
                throw new ServiceException(ErrorCode.BIND_PHONE_EXIST);
            }
            // 添加新的账号
            thirdAcc = new ThirdAcc();
            thirdAcc.setOpenId(openid);
            thirdAcc.setPhone(phone);
            thirdAcc.setAccType((byte) 1);
            thirdAccMapper.insertSelective(thirdAcc);
        }
        // 判断用户信息是否存在
        Example e = new Example(User.class);
        Example.Criteria c = e.createCriteria();
        if (StringUtils.isBlank(phone)) {
            c.andEqualTo(COLUMN_USER_PHONE,phone);
        }
        List<User> users = userMapper.selectByExample(e);
        // 如果没有用户信息，创建一个
        if (users!=null||users.size()==0) {
            User user = new User();
            if (superId!=null&&superId!=0) {
                promoteLevel(superId);
                try {
                    int i = RandomUtil.generateRandom(6);
                    String name = "懒"+i;
                    user.setNickName(name);
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
                user.setSuperId(superId).setUserPhone(phone).setCreateTime(DateUtils.formatDate(new Date())).setActive(1).setAge(age);
                userMapper.insertSelective(user);
                UserLevel userLevel = new UserLevel();
                userLevel.setUserId(user.getAppUserId()).setLevelId(1).setInviteNum(0);
                userLevelMapper.insertSelective(userLevel);
                if (user.getActive() != null && user.getActive() != 1) {
                    throw new ServiceException(ErrorCode.ACC_DISABLED);
                }
                return user;
            }
        }

        return null;
    }

    private void promoteLevel(Integer superId) {
        //更改上级用户等级
        UserLevel userLevel = userLevelMapper.selectByPrimaryKey(superId);
        int numNew = (userLevel.getInviteNum()) + 1;
        if (numNew>5&&numNew<=15) {
            int leveIdNew = userLevel.getLevelId() + 1;
            userLevel.setInviteNum(numNew).setLevelId(leveIdNew);
            userLevelMapper.updateByPrimaryKeySelective(userLevel);
        }
        if (numNew>15&&numNew<=30) {
            int leveIdNew = userLevel.getLevelId() + 1;
            userLevel.setInviteNum(numNew).setLevelId(leveIdNew);
            userLevelMapper.updateByPrimaryKeySelective(userLevel);
        }
        if (numNew>30&&numNew<=50) {
            int leveIdNew = userLevel.getLevelId() + 1;
            userLevel.setInviteNum(numNew).setLevelId(leveIdNew);
            userLevelMapper.updateByPrimaryKeySelective(userLevel);
        }
        if (numNew>50) {
            int leveIdNew = userLevel.getLevelId() + 1;
            userLevel.setInviteNum(numNew).setLevelId(leveIdNew);
            userLevelMapper.updateByPrimaryKeySelective(userLevel);
        }
    }
}
