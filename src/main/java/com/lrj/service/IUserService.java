package com.lrj.service;

import com.lrj.pojo.User;

import java.util.List;

/**
 * @author Lxh
 * @date 2020/4/7 14:30
 */
public interface IUserService {
    List<User> findUserByPhone(String phoneNum);

}
