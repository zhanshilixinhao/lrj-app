package com.lrj.mapper;

import com.lrj.pojo.User;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author Lxh
 * @date 2020/4/7 14:25
 */
public interface UserMapper extends Mapper<User>, MySqlMapper<User> {
}
