package com.lrj.mapper;

import com.lrj.pojo.ThirdAcc;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Description:
 * @Author: Lxh
 * @Date: 2020/6/9 11:56
 */
@Repository
public interface ThirdAccMapper extends Mapper<ThirdAcc>, MySqlMapper<ThirdAcc> {
}
