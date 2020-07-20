package com.lrj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lrj.VO.*;
import com.lrj.pojo.Balance;
import com.lrj.pojo.BalanceRecord;
import com.lrj.pojo.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;
import java.util.Map;

/**
 * @author Lxh
 * @date 2020/4/7 14:25
 */
@Repository
public interface UserMapper extends Mapper<User>, MySqlMapper<User>, BaseMapper<User> {
    List<ConsigneeVo> findUserAddressByUserId(Integer userId);

    UserInfoVo getUserInfoByUserPhone(String userPhone);

    UserInfoVo getUserByInviteCode(String inviteCode);

    Integer addUser(UserInfoVo userPhone);

    List<UserCouponVo> getUserRedPacket(Integer userId);

    UserLevelVo getUserLevelInfo(Integer userId);

    void updateCoupon(Integer couponId);

    UserInfoVo getUserInfoByUserId(Integer userId);

    Integer giveFeeBack(Map<String, Object> params);

    Balance getUserBalanceInfo(Integer userId);

    void sendRandomCoupon(UserCouponVo userCouponVo);

    List<UserRebateVo> getUserRebate(Integer userId);

    List<UserInfoVo> getMyInvitePeople(Integer userId);

    Integer addUserAliAccount(UserInfoVo userInfoVo);

    Integer userWithdraw(UserRebateVo userRebateVo);

    List<BalanceRecord> getUserBalanceRecordList(Integer userId);

    void addUserLeaveMessage(Map<String, Object> params);

    void selfHelpClaims(Map<String, Object> params);
}
