package com.lrj.mapper;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.AppShoppingVo;
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

    List<AppShoppingVo> getShoppingDetails(Integer userId);
}
