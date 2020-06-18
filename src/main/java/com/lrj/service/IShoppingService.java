package com.lrj.service;

import com.lrj.VO.ShoppingVo;
import com.lrj.pojo.AppShoppingEntity;

import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 购物车
 * @date : 2020-4-1
 */
public interface IShoppingService {

    /**
     * 通过userId 查询该用户购物车具体
     * @param userId
     * @return
     */
    List<ShoppingVo> getShoppingDetails(Integer userId);

    /**
     * 通过userId,ItemId购物车 明细
     * @param paramMap
     * @return
     */
    List<ShoppingVo> findUserShopping(Map<String, Integer> paramMap);

    /**
     * 更新购物车数量
     * @param paramMap
     */
    void updateUserShopping(Map<String, Integer> paramMap);

    /**
     * 添加商品 到购物车
     * @param shoppingVo
     */
    void insertAppShopping(ShoppingVo shoppingVo);

    /**
     * 购物车商品数量保存
     * @param paramMap
     */
    void updateUserShoppingQuantity(Map<String, Integer> paramMap);

    /**
     * 清空购物车
     * @param userId
     */
    void emptyShopCart(Integer userId);

    /**
     * 滑动删除购物车里的商品
     * @param shoppingId
     */
    void deleteShoppingId(Integer shoppingId);

    /**
     * 商品增值服务
     * @param appShoppingEntityEntity
     */
    void updateUserShoppingSupportValue(AppShoppingEntity appShoppingEntityEntity);
}
