package com.lrj.service;

import com.lrj.pojo.Activity;
import com.lrj.pojo.DistributionStation;

import java.util.List;

/**
 * @author : cwj
 * @describe : 配送站
 * @date : 2020-7-28
 */
public interface IDistributionStationService {


    /**
     * 查询所有配送站的信息
     * @return
     */
    List<DistributionStation> getDistriButionStationList();

    /**
     *根据 配送站ID 查询配送站信息
     * @param distributionStationId
     * @return
     */
    DistributionStation getDistriButionStationById(String distributionStationId);
}
