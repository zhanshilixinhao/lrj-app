package com.lrj.service;

import com.lrj.VO.AreaManagement;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : cwj
 * @describe : 获取基础面积-价格
 * @date : 2020-7-17
 */
public interface IAreaManagementService {
    /**
     * @Description: 获取基础价格
     * @Author: LxH
     * @Date: 2020/5/28 10:13
     */
     List<AreaManagement> findBasisArea();
}
