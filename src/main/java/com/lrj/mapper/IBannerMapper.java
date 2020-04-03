package com.lrj.mapper;

import com.lrj.VO.BannerVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : cwj
 * @describe : 首页轮播图
 * @date : 2020-4-1
 */
@Repository
public interface IBannerMapper {

    /**
     * 查询首页轮播图 所有
     * @return
     */
    List<BannerVo> getBannerList();
}
