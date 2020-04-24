package com.lrj.mapper;


import com.lrj.pojo.Order;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author Lxh
 * @date 2020/4/17 14:09
 */
public interface CardOrderMapper extends Mapper<Order> , MySqlMapper<Order> {
}
