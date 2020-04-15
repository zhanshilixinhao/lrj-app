package com.lrj.service.impl;

import com.lrj.VO.ConsigneeVo;
import com.lrj.VO.HouseServiceVo;
import com.lrj.VO.UserInfoVo;
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



    public List<HouseServiceVo> findHouseService(Integer typeId) {
        return null;
    }
}
