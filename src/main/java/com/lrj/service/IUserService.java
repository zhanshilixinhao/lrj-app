package com.lrj.service;

import com.lrj.VO.*;
import com.lrj.dto.RequestDTO;
import com.lrj.pojo.Balance;
import com.lrj.pojo.BalanceRecord;
import com.lrj.pojo.User;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;
import java.util.Map;

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

    List<UserCouponVo> findUserRedPacket(Integer userId);

    UserLevelVo findUserLevelInfo(Integer userId);

    void updateCoupon(Integer couponId);

    UserInfoVo findUserInfoByUserId(Integer userId);

    Integer giveFeeBack(Map<String, Object> params);

    Balance getUserBalanceInfo(Integer userId);

    void sendRandomCoupon(Integer userId,Integer source);

    List<UserRebateVo> getUserRebate(Integer userId);

   User getAppUserByParam(User user);

    List<UserInfoVo> getMyInvitePeople(Integer userId);

    Integer addUserAliAccount(UserInfoVo userInfoVo);

    Integer userWithdraw( UserRebateVo userRebateVo);

    List<BalanceRecord> getUserBalanceRecordList(Integer userId);

    void addUserLeaveMessage(Map<String, Object> params);

    void selfHelpClaims(Map<String, Object> params);
}
