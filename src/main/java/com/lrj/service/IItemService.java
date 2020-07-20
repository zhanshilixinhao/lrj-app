package com.lrj.service;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.BannerVo;
import com.lrj.VO.ResultVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 商品 服务
 * @date : 2020-4-1
 */
public interface IItemService {

    /**
     * 通过商品类ID  查询该类别下的所有商品
     * @return
     * @param paramMap
     */
    List<AppItemVo> getItemListByCategoryId(Map<String, Integer> paramMap);

    /**
     * 根据商品ID获取商品的顶层类别ID
     * @param itemId
     * @return
     */
    Integer getItemCategoryByItemId(Integer itemId);

    /**
     * 查询限时特价商品
     * @return
     */
    List<AppItemVo> getSpecialItem();
}
