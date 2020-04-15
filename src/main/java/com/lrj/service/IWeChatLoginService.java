package com.lrj.service;

/**
 * @author Lxh
 * @date 2020/4/14 17:26
 */
public interface IWeChatLoginService {
    void findUserByOpenId(String openId,String userInfo);
}
