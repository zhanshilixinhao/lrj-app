package com.lrj.mapper;

import com.lrj.pojo.Level;
import com.sun.org.apache.bcel.internal.generic.INEG;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Description:
 * @Author: Lxh
 * @Date: 2020/5/26 11:53
 */
@Repository
public interface LevelMapper extends Mapper<Level>, MySqlMapper<Level> {
    /**
     * 根据Id 查询等级信息
     * @return
     */
    Level findLevelByLeaveId(Integer levelId);
}
