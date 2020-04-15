package com.lrj.service;

import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.UserInfoVo;
import com.lrj.pojo.User;

import java.util.List;

/**
 * @author Lxh
 * @date 2020/4/7 14:30
 */
public interface IUserService {
    List<User> findUserByPhone(String phoneNum);

    UserInfoVo findUserInfoByUserPhone(String userPhone);

    UserInfoVo findUserByInviteCode(String inviteCode);

    Integer addUser(UserInfoVo setCreateTime);

    List<ConsigneeVo> findUserAddressByUserId(Integer userId);
}
