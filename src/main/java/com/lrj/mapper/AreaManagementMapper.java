package com.lrj.mapper;

import com.lrj.VO.AreaManagement;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Description:
 * @Author Lxh
 * @Date 2020/5/27 15:17
 */
@Repository
public interface AreaManagementMapper extends Mapper<AreaManagement> , MySqlMapper<AreaManagement> {
}
