package com.lrj.service.impl;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.ItemCategoryVo;
import com.lrj.mapper.IItemCategoryMapper;
import com.lrj.mapper.IItemMapper;
import com.lrj.service.IItemCategoryService;
import com.lrj.service.IItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 商品类别
 * @date : 2020-4-1
 */
@Service
@Transactional
public class ItemCategoryServiceImpl implements IItemCategoryService{

    @Resource
    private IItemCategoryMapper itemCategoryMapper;

    public List<ItemCategoryVo> getItemCategory(Integer pid) {
        return itemCategoryMapper.getItemCategory(pid);
    }
}
