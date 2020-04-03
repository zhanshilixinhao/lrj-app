package com.lrj.service.impl;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.ValueAddedServicesVo;
import com.lrj.mapper.IItemMapper;
import com.lrj.mapper.IValueAddedServicesMapper;
import com.lrj.service.IItemService;
import com.lrj.service.IValueAddedServicesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 增值服务
 * @date : 2020-4-1
 */
@Service
@Transactional
public class IValueAddedServicesServiceImpl implements IValueAddedServicesService{

    @Resource
    private IValueAddedServicesMapper valueAddedServicesMapper;

    public List<ValueAddedServicesVo> listValueAddedServices() {
        return valueAddedServicesMapper.listValueAddedServices();
    }
}
