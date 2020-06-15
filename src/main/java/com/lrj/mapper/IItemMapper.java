package com.lrj.mapper;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.ItemCategoryVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 商品
 * @date : 2020-4-2
 */
@Repository
public interface IItemMapper {
    /**
     * 通过商品类别Id查询该类下的所有商品
     * @param paramMap
     * @return
     */
    List<AppItemVo> getItemListByCategoryId(Map<String,Integer> paramMap);

    /**
     * 根据商品ID获取商品的顶层类别ID
     * @param itemId
     * @return
     */
    Integer getItemCategoryByItemId(Integer itemId);

    /**
     * 通过itemId  查询商品的相应信息
     */
    AppItemVo getItemInfoByItemId(Integer itemId);

    /**
     * 通过 itemCategory 查询商品类别的信息
     */
    ItemCategoryVo getItemCategoryInfoByCategoryId(Integer itemCategoryId);
}
