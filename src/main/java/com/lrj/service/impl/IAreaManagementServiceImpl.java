package com.lrj.service.impl;

import com.lrj.VO.AreaManagement;
import com.lrj.mapper.AreaManagementMapper;
import com.lrj.service.IAreaManagementService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : cwj
 * @describe :获取基础面积-价格
 * @date : 2020-7-17
 */
@Service
public class IAreaManagementServiceImpl implements IAreaManagementService{
    @Resource
    private AreaManagementMapper areaManagementMapper;
    /**
     * @Description: 获取基础价格
     * @Author: LxH
     * @Date: 2020/5/28 10:13
     */
    @Override
    public List<AreaManagement> findBasisArea() {
        Example example = new Example(AreaManagement.class);
        example.createCriteria().andEqualTo("itemId", 388);
        return areaManagementMapper.selectAll();
    }
}
