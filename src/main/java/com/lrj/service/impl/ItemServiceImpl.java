package com.lrj.service.impl;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.BannerVo;
import com.lrj.mapper.IBannerMapper;
import com.lrj.mapper.IItemMapper;
import com.lrj.service.IBannerService;
import com.lrj.service.IItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 商品
 * @date : 2020-4-1
 */
@Service
@Transactional
public class ItemServiceImpl implements IItemService{

    @Resource
    private IItemMapper itemMapper;

    @Override
    public List<AppItemVo> getItemListByCategoryId(Map<String,Integer> paramMap) {
        return itemMapper.getItemListByCategoryId(paramMap);
    }

    /**
     * 根据商品ID获取商品的顶层类别ID
     * @param itemId
     * @return
     */
    @Override
    public Integer getItemCategoryByItemId(Integer itemId) {
        return itemMapper.getItemCategoryByItemId(itemId);
    }
}
