package com.lrj.service.impl;

import com.lrj.VO.ShoppingVo;
import com.lrj.mapper.IShoppingMapper;
import com.lrj.pojo.AppShoppingEntity;
import com.lrj.service.IShoppingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 购物车
 * @date : 2020-4-1
 */
@Service
@Transactional
public class ShoppingServiceImpl implements IShoppingService{


    @Resource
    private IShoppingMapper shoppingMapper;

    public List<ShoppingVo> getShoppingDetails(Integer userId) {
        return shoppingMapper.getShoppingDetails(userId);
    }

    public List<ShoppingVo> findUserShopping(Map<String, Integer> paramMap) {
        return shoppingMapper.getUserShopping(paramMap);
    }

    public void updateUserShopping(Map<String, Integer> paramMap) {
        shoppingMapper.updateUserShopping(paramMap);
    }

    public void insertAppShopping(ShoppingVo shoppingVo) {
        shoppingMapper.insertAppShopping(shoppingVo);
    }

    public void updateUserShoppingQuantity(Map<String, Integer> paramMap) {
        shoppingMapper.updateUserShoppingQuantity(paramMap);
    }

    public void emptyShopCart(Integer userId) {
        shoppingMapper.emptyShopCart(userId);
    }

    public void deleteShoppingId(Integer shoppingId) {
        shoppingMapper.deleteShoppingId(shoppingId);
    }

    @Override
    public void updateUserShoppingSupportValue(AppShoppingEntity appShoppingEntityEntity) {
        shoppingMapper.updateUserShoppingSupportValue(appShoppingEntityEntity);
    }
}
