package com.lrj.mapper;

import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.UserInfoVo;
import com.lrj.pojo.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * @author Lxh
 * @date 2020/4/7 14:25
 */
@Repository
public interface UserMapper extends Mapper<User>, MySqlMapper<User> {
    List<ConsigneeVo> findUserAddressByUserId(Integer userId);

    UserInfoVo getUserInfoByUserPhone(String userPhone);

    UserInfoVo getUserByInviteCode(String inviteCode);

    Integer addUser(UserInfoVo userPhone);
}
