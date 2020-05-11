package com.lrj.mapper;

import com.lrj.pojo.Reservation;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;
import java.util.Map;

/**
 * @author Lxh
 * @date 2020/4/23 17:13
 */
@Repository
public interface ReservationMapper extends Mapper<Reservation>, MySqlMapper<Reservation> {
    /**
     * 查询待抢的洗衣订单
     */
    List<Reservation> getWashingOrderList();

    /**
     * 查询待抢的家政订单
     * @return
     */
    List<Reservation> getHouseServiceOrderList();

    /**
     * 锁定 预约订单(并绑定锁单人)
     * @param orderNumber
     */
    Integer lockReservation(String orderNumber,Integer staffId);

    /**
     * 更新预约订单 状态
     * @param params
     * @return
     */
    Integer updateReservationStatus(Map<String, Object> params);
}
