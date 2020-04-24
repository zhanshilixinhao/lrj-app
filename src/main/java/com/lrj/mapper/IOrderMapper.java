package com.lrj.mapper;


import com.lrj.VO.OrderVo;

import com.lrj.pojo.Order;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * @author : cwj
 * @describe : 订单 有关接口
 * @date : 2020-4-2
 */

public interface IOrderMapper  {
    /**
     * 创建订单
     */
    Integer createOrder(OrderVo orderVo);

    /**
     * 通过订单Id查询订单数据
     * @return
     */
    OrderVo getOrderByOrderId(Integer appOrderId);



}
