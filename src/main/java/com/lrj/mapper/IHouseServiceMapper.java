package com.lrj.mapper;

import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.HouseServiceVo;
import com.lrj.VO.HouserServicePidVo;
import com.lrj.VO.UserInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 家政服务 相关接口
 * @date : 2020-4-2
 */
@Repository
public interface IHouseServiceMapper {
    /**
     * 通过家政类型查询家政服务项目
     */
    List<HouseServiceVo> getHouseService(Map<String,Integer> params);

    /**
     * 通过家政itemId 查询单独的家政服务
     */
    HouseServiceVo getHouseServiceByItemId(Integer houseServiceId);

    /**
     * 获取家政导航栏
     * @return
     */
    List<HouserServicePidVo> getHouseServicePidList();

    /**
     * 查询家政个性服务
     * @return
     */
    List<HouseServiceVo> getCustomHouseService(Integer areaType);
}
