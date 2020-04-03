package com.lrj.service;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.AppShoppingVo;

import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 购物车
 * @date : 2020-4-1
 */
public interface IShoppingService {

    /**
     * 通过userId 查询该用户购物车
     * @param userId
     * @return
     */
    List<AppShoppingVo> getShoppingDetails(Integer userId);
}
