package com.lrj.mapper;

import com.lrj.VO.ShoppingVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 购物车
 * @date : 2020-4-2
 */
@Repository
public interface IShoppingMapper {

    List<ShoppingVo> getShoppingDetails(Integer userId);

    List<ShoppingVo> getUserShopping(Map<String, Integer> paramMap);

    void updateUserShopping(Map<String, Integer> paramMap);

    void insertAppShopping(ShoppingVo shoppingVo);

    void updateUserShoppingQuantity(Map<String, Integer> paramMap);

    void emptyShopCart(Integer userId);

    void deleteShoppingId(Integer shoppingId);
}
