package com.lrj.service.impl;

import com.lrj.VO.*;
import com.lrj.mapper.IItemMapper;
import com.lrj.mapper.IUserMapper;
import com.lrj.mapper.UserMapper;
import com.lrj.pojo.User;
import com.lrj.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
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

    /*@Resource
    private IUserMapper userMapper;*/
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
