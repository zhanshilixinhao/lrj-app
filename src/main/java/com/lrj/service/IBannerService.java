package com.lrj.service;

import com.lrj.VO.BannerVo;

import java.util.List;

/**
 * @author : cwj
 * @describe : 首页轮播图 服务
 * @date : 2020-4-1
 */
public interface IBannerService {

    List<BannerVo> getBannerList();
}
