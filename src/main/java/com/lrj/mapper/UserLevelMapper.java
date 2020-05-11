package com.lrj.mapper;

import com.lrj.pojo.UserLevel;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author Lxh
 * @date 2020/4/30 15:14
 */
@Repository
public interface UserLevelMapper extends Mapper<UserLevel>, MySqlMapper<UserLevel> {
}
