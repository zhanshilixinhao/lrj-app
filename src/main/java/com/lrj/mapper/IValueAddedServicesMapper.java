package com.lrj.mapper;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.ValueAddedServicesVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 增值服务
 * @date : 2020-4-2
 */
@Repository
public interface IValueAddedServicesMapper {


    List<ValueAddedServicesVo> listValueAddedServices();
}
