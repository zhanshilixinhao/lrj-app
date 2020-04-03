package com.lrj.service;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.ItemCategoryVo;

import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 商品类别 服务
 * @date : 2020-4-1
 */
public interface IItemCategoryService {


    /**
     * 查询商品类别
     * @return
     */
    List<ItemCategoryVo> getItemCategory(Integer pid);
}
