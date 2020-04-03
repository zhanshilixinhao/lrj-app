package com.lrj.service.impl;

import com.lrj.VO.BannerVo;
import com.lrj.mapper.IBannerMapper;
import com.lrj.service.IBannerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : cwj
 * @describe : 首页轮播图
 * @date : 2020-4-1
 */
@Service
@Transactional
public class BannerServiceImpl implements IBannerService{

    @Resource
    private IBannerMapper bannerMapper;
    public List<BannerVo> getBannerList() {
        return bannerMapper.getBannerList();
    }
}
