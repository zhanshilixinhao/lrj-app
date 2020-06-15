package com.lrj.service.impl;

import com.lrj.VO.*;
import com.lrj.mapper.UserMapper;
import com.lrj.pojo.Balance;
import com.lrj.pojo.User;
import com.lrj.service.IUserService;
import com.lrj.util.DateUtils;
import com.lrj.util.RandomUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 用户服务
 * @date : 2020-4-1
 */
@Service
@Transactional
public class IUserServiceImpl implements IUserService{

    @Resource
    private UserMapper userMapper;



    public List<ConsigneeVo> findUserAddressByUserId(Integer userId) {
        return userMapper.findUserAddressByUserId(userId);
    }


    public List<UserCouponVo> findUserRedPacket(Integer userId) {
        return userMapper.getUserRedPacket(userId);
    }


    public UserLevelVo findUserLevelInfo(Integer userId) {
        return userMapper.getUserLevelInfo(userId);
    }


    public void updateCoupon(Integer couponId) {
        userMapper.updateCoupon(couponId);
    }


    public UserInfoVo findUserInfoByUserId(Integer userId) {
        return userMapper.getUserInfoByUserId(userId);
    }


    public Integer giveFeeBack(Map<String, Object> params) {
       return userMapper.giveFeeBack(params);
    }

    public Balance getUserBalanceInfo(Integer userId) {
        return userMapper.getUserBalanceInfo(userId);
    }

    //赠送随机红包
    public void sendRandomCoupon(Integer userId,Integer source) {
        UserCouponVo userCouponVo = new UserCouponVo();
        userCouponVo.setActive(0);
        userCouponVo.setUserId(userId);
        userCouponVo.setCreateTime(DateUtils.getNowTime("YYYY-MM-DD HH:MM:SS"));
           //订单分享获得红包
        userCouponVo.setCouponType(1);
        userCouponVo.setLimitTime(DateUtils.getParamDateAfterNDays(userCouponVo.getCreateTime(), 30));
        //获取红包面额
        String rString = RandomUtil.getRandomCopuon();
        userCouponVo.setDenomination(Integer.parseInt(rString));
        userCouponVo.setSource(source);
        if(source==2){
            userCouponVo.setUseInstructions("订单分享后获得,需分享人员使用才生效");
        }else if(source==4){
            userCouponVo.setUseInstructions("邀请新用户所得,需新用户使用后才生效");
        }

        userMapper.sendRandomCoupon(userCouponVo);
    }

    public List<UserRebateVo> getUserRebate(Integer userId) {
       List<UserRebateVo> userRebateVoList =  userMapper.getUserRebate(userId);
       //拼接Id关联的信息
        UserInfoVo userInfoVo = userMapper.getUserInfoByUserId(userId);
        for(UserRebateVo userRebateVo:userRebateVoList){
            UserInfoVo userInfoVoLow = userMapper.getUserInfoByUserId(userId);
            userRebateVo.setUserName(userInfoVo.getNickname());
            userRebateVo.setLowUserName(userInfoVoLow.getNickname());
        }
        return userRebateVoList;
    }


    public UserInfoVo findUserInfoByUserPhone(String userPhone) {
        return userMapper.getUserInfoByUserPhone(userPhone);
    }



    public UserInfoVo findUserByInviteCode(String inviteCode) {
        return userMapper.getUserByInviteCode(inviteCode);
    }



    public Integer addUser(UserInfoVo userPhone) {
        return userMapper.addUser(userPhone);
    }




    public List<User> findUserByPhone(String phoneNum) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userPhone",phoneNum);
        List<User> users = userMapper.selectByExample(example);
        return users;
    }
}
