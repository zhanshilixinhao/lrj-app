package com.lrj.service.impl;

import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.HouseServiceVo;
import com.lrj.VO.HouserServicePidVo;
import com.lrj.VO.UserInfoVo;
import com.lrj.mapper.IHouseServiceMapper;
import com.lrj.mapper.IUserMapper;
import com.lrj.service.IHouseService;
import com.lrj.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
    public List<HouseServiceVo> findHouseService(Integer itemCategoryId) {
        return houseServiceMapper.getHouseService(itemCategoryId);
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
    public List<HouseServiceVo> findCustomHouseService() {
        return houseServiceMapper.getCustomHouseService();
    }
}
