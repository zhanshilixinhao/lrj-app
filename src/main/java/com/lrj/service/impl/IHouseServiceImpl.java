package com.lrj.service.impl;

import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.HouseServiceVo;
import com.lrj.VO.HouserServicePidVo;
import com.lrj.VO.UserInfoVo;
import com.lrj.mapper.IHouseServiceMapper;
import com.lrj.mapper.IUserMapper;
import com.lrj.service.IHouseService;
import com.lrj.service.IUserService;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 家政服务
 * @date : 2020-4-1
 */
@Service
@Transactional
public class IHouseServiceImpl implements IHouseService{

@Resource
private IHouseServiceMapper houseServiceMapper;

    /**
     * 通过家政类型查询家政服务项目
     * @param itemCategoryId
     * @return
     */
    public List<HouseServiceVo> findHouseService(Integer itemCategoryId,Integer areaType) {
        Map<String, Integer> params = new HashMap<>();
        params.put("itemCategoryId", itemCategoryId);
        params.put("areaType", areaType);
        return houseServiceMapper.getHouseService(params);
    }

    /**
     * 查询家政导航栏
     * @return
     */
    public List<HouserServicePidVo> findHouseServicePidList() {
        return houseServiceMapper.getHouseServicePidList();
    }

    /**
     * 查询家政个性服务
     * @return
     */
    public List<HouseServiceVo> findCustomHouseService(Integer areaType) {
        return houseServiceMapper.getCustomHouseService(areaType);
    }
}
