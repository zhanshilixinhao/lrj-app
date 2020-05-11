package com.lrj.mapper;

import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.HouseServiceVo;
import com.lrj.VO.UserInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    List<HouseServiceVo> getHouseService(Integer typeId);

}
