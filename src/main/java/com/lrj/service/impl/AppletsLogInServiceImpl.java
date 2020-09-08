package com.lrj.service.impl;

import com.alibaba.fastjson.JSON;
import com.lrj.VO.FormerResult;
import com.lrj.VO.MiNiUserInfo;
import com.lrj.VO.RawData;
import com.lrj.VO.WXResult;
import com.lrj.common.Constant;
import com.lrj.common.ErrorCode;
import com.lrj.exception.ServiceException;
import com.lrj.mapper.BalanceMapper;
import com.lrj.mapper.ThirdAccMapper;
import com.lrj.mapper.UserLevelMapper;
import com.lrj.mapper.UserMapper;
import com.lrj.pojo.Balance;
import com.lrj.pojo.ThirdAcc;
import com.lrj.pojo.User;
import com.lrj.pojo.UserLevel;
import com.lrj.service.AppletsLogInService;
import com.lrj.util.*;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
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

    // app获取access_tpken
    public static final String URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    // 小程序获取session
    public static final String URL_M = "https://api.weixin.qq.com/sns/jscode2session";
    // app获取用户信息
    public static final String URL_USER_INFO = "https://api.weixin.qq.com/sns/userinfo";
    // 小程序公众号获取access_token
    public static final String MINI_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
    // 小程序
    private static final String APPID = "wxc31f3dd8ecfe95a8"; // wx4e47fc336f8578df
    private static final String APP_SECRET = "1596648e33b6142167e60545540666a6";

    // app
    private static final String APPID_APP = "wx6dbf2ef384cd6f34"; // wx4e47fc336f8578df
    private static final String APP_SECRET_APP = "2df2ceb6d7fe5d79b57428a4501ebc23";

    private static final OkHttpClient CLIENT = OkHttpManager.create(null, null);



    @Value("${bind.apikey}")
    private String apiKey;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserLevelMapper userLevelMapper;

    @Resource
    private ThirdAccMapper thirdAccMapper;

    @Resource
    private BalanceMapper balanceMapper;

    private FormerResult formerResult = new FormerResult();

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
    public FormerResult login(String code, RawData userInfo) {
        // 获取微信小程序的openid
        WXResult result = getSession(3, code);
        // 如果获取失败
        if (result.getErrcode() != 0) {
            throw new ServiceException(result.getErrmsg(), result.getErrcode());
        }
        // 获取成功
        // 1.判断该openid是否存在
        Example example = new Example(ThirdAcc.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(COLUMN_THIRDACC_OPEN_ID,result.getOpenid());
        List<ThirdAcc> thirdAccs = thirdAccMapper.selectByExample(example);
        // 1.1如果账号不存在需要绑定手机号
        if (thirdAccs.size() == 0) {
            // 产生一个随机数
            String key = UUID.randomUUID().toString();
            userInfo.setOpenid(result.getOpenid());
            userCache.put(key,userInfo.getOpenid());
            Map<String, String> map = new HashMap<>();
            map.put("s1", key);
            //return "6001";
            //return CommonUtil.FAIL(formerResult,"需要绑定手机号!",ErrorCode.NEED_BIND_PHONE);
            return formerResult.setErrorTip("需要绑定手机号!").setErrorCode(6001).setRequestStatus(Constant.SUCCESS).setData(map);
            //throw new ServiceException(ErrorCode.NEED_BIND_PHONE).data(map);
            // 如果账号存在，取出用户信息
        } else {
            for (ThirdAcc thirdAcc : thirdAccs) {
                Example e = new Example(User.class);
                Example.Criteria c = e.createCriteria();
                if (thirdAcc.getPhone()!=null) {
                    c.andEqualTo(COLUMN_USER_PHONE,thirdAcc.getPhone());
                }
                List<User> users = userMapper.selectByExample(e);
                for (User user : users) {
                    if (user != null && user.getActive() != null && user.getActive() != 1) {
                        throw new ServiceException(ErrorCode.ACC_DISABLED);
                    }
                    return CommonUtil.SUCCESS(formerResult,null,user);
                }
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
    public FormerResult askCode(String phone) throws IOException {
        // 判断账号是否存在
        Example example = new Example(ThirdAcc.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(COLUMN_THIRDACC_PHONE,phone);
        List<ThirdAcc> thirdAccs = thirdAccMapper.selectByExample(example);
        if (thirdAccs.size()!=0) {
            // 如果已绑定账号
            return formerResult.setErrorTip("该手机号已绑定!").setErrorCode(6004).setRequestStatus(Constant.SUCCESS);
        }
        // 生成随机六位验证码
        String verificationCode = RandomUtil.generateOrder(6) + "";
        // 设置发送的内容(内容必须和模板匹配)
        String text = "【懒人家】您的验证码是" + verificationCode + "。如非本人操作，请忽略本短信";
        // 存储验证码
        userCache.put(phone, verificationCode);
        /** 发送短信 **/
        //SmsApi.sendSms(apiKey, text, phone);
        String str = JavaSmsApi.sendSms(apiKey, text, phone);
        System.out.println(str);
        return CommonUtil.SUCCESS(formerResult,null,"验证码:"+verificationCode+"云片ip:"+str);
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
    public FormerResult bindPhone(String phone, String code, String s1, Integer superId, Byte age, Integer merchantId, MiNiUserInfo miNiUserInfo) {
        // 判断s1是否存在
        String json = userCache.getCache(s1);
        log.info(json);
        if (org.apache.commons.lang.StringUtils.isBlank(json)) {
            //throw new ServiceException(ErrorCode.OPENID_DISABLED);
            return formerResult.setErrorTip("操作无效或过期!").setErrorCode(6002).setRequestStatus(Constant.SUCCESS);
        }
        // RawData wxUserInfo = JSON.parseObject(json, RawData.class);
        // String openid = wxUserInfo.getOpenid();
        String openid = json;

        // 判断验证码
        String currentCode = userCache.getCache(phone);
        if (!org.apache.commons.lang.StringUtils.equals(currentCode, code)) {
            //throw new ServiceException(ErrorCode.VERIFY_CODE_ERR);
            return formerResult.setErrorTip("短信验证码错误!").setErrorCode(6003).setRequestStatus(Constant.SUCCESS);
        }
        // 判断账号是否存在
        Example example = new Example(ThirdAcc.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(COLUMN_THIRDACC_PHONE,phone);
        List<ThirdAcc> thirdAccs = thirdAccMapper.selectByExample(example);
        if (thirdAccs.size()!=0) {
            // 如果已绑定账号
            return formerResult.setErrorTip("该手机号已绑定!").setErrorCode(6004).setRequestStatus(Constant.SUCCESS);
        }
        // 添加新的账号
        ThirdAcc thirdAcc = new ThirdAcc();
        thirdAcc.setOpenId(openid);
        thirdAcc.setPhone(phone);
        thirdAcc.setAccType((byte) 1);
        thirdAccMapper.insertSelective(thirdAcc);
        // 判断用户信息是否存在
        Example e = new Example(User.class);
        Example.Criteria c = e.createCriteria();
        c.andEqualTo(COLUMN_USER_PHONE,phone);
        List<User> users = userMapper.selectByExample(e);
        if (users.size()==0) {
            User user = new User();
            if (superId!=null&&superId!=0) {
                promoteLevel(superId);
                user.setSuperId(superId).setSuperType(1);
            }
            if (merchantId!=null&&merchantId!=0) {
                user.setSuperId(merchantId).setSuperType(2);
            }
            try {
                int i = RandomUtil.generateRandom(6);
                String name = "懒"+i;
                user.setNickName(name);
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
            user.setUserPhone(phone).setCreateTime(DateUtils.formatDate(new Date())).setActive(1).setAge(age).setHeadPhoto(miNiUserInfo.getAvatarUrl()).
            setNickName(miNiUserInfo.getNickname());
            userMapper.insertSelective(user);
            UserLevel userLevel = new UserLevel();
            userLevel.setUserId(user.getAppUserId()).setLevelId(1).setInviteNum(0);
            userLevelMapper.insertSelective(userLevel);
            Balance balance = new Balance();
            balance.setUserId(user.getAppUserId()).setBalance(new BigDecimal("0.00")).
                    setCreateTime(DateUtils.formatDate(new Date()));
            balanceMapper.insertSelective(balance);
            if (user.getActive() != null && user.getActive() != 1) {
                //throw new ServiceException(ErrorCode.ACC_DISABLED);
                return formerResult.setErrorTip("该账号已被禁用").setErrorCode(5005).setRequestStatus(Constant.SUCCESS);
            }
            return CommonUtil.SUCCESS(formerResult,null,user);
        }else {
            for (User user : users) {
                return CommonUtil.SUCCESS(formerResult,null,user);
            }
        }
        return null;
    }

    private void promoteLevel(Integer superId) {
        //更改上级用户等级
        UserLevel userLevel = userLevelMapper.selectByPrimaryKey(superId);
        int numNew;
        if (userLevel.getInviteNum()==null) {
            numNew=1;
        }else {
            numNew = (userLevel.getInviteNum()) + 1;
        }
        if (numNew<=5) {
            userLevel.setInviteNum(numNew);
            userLevelMapper.updateByPrimaryKeySelective(userLevel);
        }
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
    public WXResult getSession(Integer clientApp, String code) {
        String codeKey;
        String url;
        String appId;
        String secretKey;
        if (clientApp != 3) {
            codeKey = "code";
            url = URL;
            appId = APPID_APP;
            secretKey = APP_SECRET_APP;
        } else {
            codeKey = "js_code";
            url = URL_M;
            appId = APPID;
            secretKey = APP_SECRET;
        }

        RequestParams params = new RequestParams();
        params.put("appid", appId);
        params.put("secret", secretKey);
        params.put(codeKey, code);
        params.put("grant_type", "authorization_code");
        return wxRequest(url, params);
    }

    /**
     * 微信接口请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    private WXResult wxRequest(String url, RequestParams params) {
        return wxRequest(url, params, 0);
    }

    /**
     * 微信接口请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    private WXResult wxRequest(String url, RequestParams params, int method) {
        WXResult result = new WXResult();
        try {
            Response response;
            if (method == 0) {
                response = OkHttpUtil.get(CLIENT, url, params);
            } else {
                response = OkHttpUtil.postJson(CLIENT, url, params);
            }
            if (response.isSuccessful()) {
                String re = response.body().string();
//                log.info("微信换取openid:{}", re);
                result = JSON.parseObject(re, WXResult.class);
            } else {
                result.setErrcode(1);
                result.setErrmsg(response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.setErrcode(1);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }

}
