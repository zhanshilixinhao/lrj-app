package com.lrj.mapper;

import com.lrj.VO.HouseServiceWorkTimeVo;
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
     * 查询用户的所有预约单
     */
     List<Reservation> getReservationListByUserId(Integer userId);
    /**
     * 保存预约 单
     * @param reservation
     */
    Integer insertReservation(Reservation reservation);
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

    /**
     * 根据 预约单状态和员工账号 查询预约单
     * @param status
     * @param staffId
     * @return
     */
    List<Reservation> getReservationList(Integer status, Integer staffId);

    /**
     * 查询家政服务工作时长
     * @param staffId
     * @return
     */
    List<HouseServiceWorkTimeVo> getHouseServiceWorkTime(Integer staffId);
}
