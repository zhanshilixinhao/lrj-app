package com.lrj.service.impl;

import com.lrj.mapper.IActivityMapper;
import com.lrj.mapper.IDistributionStationMapper;
import com.lrj.pojo.Activity;
import com.lrj.pojo.DistributionStation;
import com.lrj.service.IActivityService;
import com.lrj.service.IDistributionStationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : cwj
 * @describe : 活动实现类
 * @date : 2020-7-28
 */
@Service
@Transactional
public class IDistributionStationServiceImpl implements IDistributionStationService {

    @Resource
    private IDistributionStationMapper distributionStationMapper;

    /**
     * 查询所有配送站信息
     */
    @Override
    public List<DistributionStation> getDistriButionStationList() {
        return distributionStationMapper.getDistriButionStationList();
    }

    @Override
    public DistributionStation getDistriButionStationById(String distributionStationId) {
        Integer distributionStationIdInteger = Integer.parseInt(distributionStationId);

        return distributionStationMapper.getDistriButionStationById(distributionStationIdInteger);
    }
}
