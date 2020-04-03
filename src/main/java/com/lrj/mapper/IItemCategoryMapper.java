package com.lrj.mapper;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.ItemCategoryVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 商品类别
 * @date : 2020-4-2
 */
@Repository
public interface IItemCategoryMapper {
    /**
     * 查询 所有商品类别
     * @return
     */
    List<ItemCategoryVo> getItemCategory(Integer pid);
}
