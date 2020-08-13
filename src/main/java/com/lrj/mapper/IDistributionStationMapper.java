package com.lrj.mapper;

import com.lrj.pojo.Activity;
import com.lrj.pojo.DistributionStation;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : cwj
 * @describe : 配送站 接口
 * @date : 2020-7-28
 */
@Repository
public interface IDistributionStationMapper {

    /**
     * 获取所有配送站信息
     * @return
     */
    List<DistributionStation> getDistriButionStationList();

    /**
     * 根据Id 查询配送站信息
     * @param distributionStationId
     * @return
     */
    DistributionStation getDistriButionStationById(Integer distributionStationId);
}
