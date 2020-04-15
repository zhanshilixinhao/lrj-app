package com.lrj.mapper;

import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.UserInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : cwj
 * @describe : 用户 有关接口
 * @date : 2020-4-2
 */
@Repository
public interface IUserMapper {
    /**
     * 通过用户ID查询用户拥有地址
     * @return
     */
    List<ConsigneeVo> findUserAddressByUserId(Integer userId);

    /**
     * 通过手机号查询用户信息
     * @param userPhone
     * @return
     */
    UserInfoVo getUserInfoByUserPhone(String userPhone);

    /**
     * 通过邀请码 查询用户
     * @param inviteCode
     * @return
     */
    UserInfoVo getUserByInviteCode(String inviteCode);

    /**
     * 通过手机号进行注册
     * @param userPhone
     * @return
     */
    Integer addUser(UserInfoVo userPhone);
}
