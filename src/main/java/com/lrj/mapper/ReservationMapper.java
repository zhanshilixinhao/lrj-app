package com.lrj.mapper;

import com.lrj.pojo.Reservation;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author Lxh
 * @date 2020/4/23 17:13
 */
public interface ReservationMapper extends Mapper<Reservation>, MySqlMapper<Reservation> {
}
