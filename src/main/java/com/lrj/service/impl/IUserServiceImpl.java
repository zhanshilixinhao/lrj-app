package com.lrj.service.impl;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.UserInfoVo;
import com.lrj.mapper.IItemMapper;
import com.lrj.mapper.IUserMapper;
import com.lrj.mapper.UserMapper;
import com.lrj.pojo.User;
import com.lrj.service.IUserService;
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

    /*@Resource
    private IUserMapper userMapper;*/
    @Resource
    private UserMapper userMapper;

    @Override
    public List<ConsigneeVo> findUserAddressByUserId(Integer userId) {
        return userMapper.findUserAddressByUserId(userId);
    }

    @Override
    public UserInfoVo findUserInfoByUserPhone(String userPhone) {
        return userMapper.getUserInfoByUserPhone(userPhone);
    }

    @Override
    public UserInfoVo findUserByInviteCode(String inviteCode) {
        return userMapper.getUserByInviteCode(inviteCode);
    }

    @Override
    public Integer addUser(UserInfoVo userPhone) {
        return userMapper.addUser(userPhone);
    }


    @Override
    public List<User> findUserByPhone(String phoneNum) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userPhone",phoneNum);
        List<User> users = userMapper.selectByExample(example);
        return users;
    }
}
