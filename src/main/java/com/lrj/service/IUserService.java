package com.lrj.service;

import com.lrj.VO.ConsigneeVo;

import java.util.List;

/**
 * @author : cwj
 * @describe : 用户服务
 * @date : 2020-4-3
 */
public interface IUserService {

    /**
     * 通过userId 查询用户地址
     * @param userId
     * @return
     */
    List<ConsigneeVo> findUserAddressByUserId(Integer userId);
}
