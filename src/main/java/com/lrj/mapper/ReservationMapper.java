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
     * 根据预约单Id  查询预约单
     * @param reservationId
     * @return
     */
    Reservation getReservationByReservationId(Integer reservationId);
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
     * @param params
     */
    Integer lockReservation(Map<String,Object> params);

    /**
     * 更新预约订单 追踪状态
     * @param params
     * @return
     */
    Integer updateReservationTrackingStatus(Map<String, Object> params);

    /**
     * 更新预约单 状态
     */
    Integer  updateReservationStatus(Map<String, Object> params);

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


    /**
     * 通过员工ID 和 对应状态查询取件-服务单
     * @param params
     * @return
     */
    List<Reservation> getReservationByStatus1(Map<String,Object> params);

    /**
     * 通过ID 和 对应状态查询 送件-服务单
     * @param params
     * @return
     */
    List<Reservation> getReservationByStatus2(Map<String, Object> params);
    /**
     * 通过ID 和 对应状态查询完成的服务单
     * @param params
     * @return
     */
    List<Reservation> getReservationByStatus3(Map<String, Object> params);

    /**
     * 保存计算出的服务单与配送站的距离等信息
     * @param reservation
     */
    void updateReservationDistribution(Reservation reservation);
}
