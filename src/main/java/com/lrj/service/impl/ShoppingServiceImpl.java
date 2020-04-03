package com.lrj.service.impl;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.AppShoppingVo;
import com.lrj.mapper.IItemMapper;
import com.lrj.mapper.IShoppingMapper;
import com.lrj.service.IItemService;
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

    public List<AppShoppingVo> getShoppingDetails(Integer userId) {
        return shoppingMapper.getShoppingDetails(userId);
    }
}
