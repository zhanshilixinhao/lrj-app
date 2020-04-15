package com.lrj.service.impl;

import com.lrj.mapper.UserMapper;
import com.lrj.pojo.User;
import com.lrj.service.IUserService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Lxh
 * @date 2020/4/7 14:30
 */
@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> findUserByPhone(String phoneNum) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userPhone",phoneNum);
        List<User> users = userMapper.selectByExample(example);
        return users;
    }
}
