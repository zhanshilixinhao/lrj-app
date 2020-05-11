package com.lrj.service;

import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.HouseServiceVo;
import com.lrj.VO.UserInfoVo;

import java.util.List;

/**
 * @author : cwj
 * @describe : 家政服务
 * @date : 2020-4-3
 */
public interface IHouseService {

    /**
     * 通过家政类型查询家政服务项目
     */
    List<HouseServiceVo> findHouseService(Integer pid);
}
