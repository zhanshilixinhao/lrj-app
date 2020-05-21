package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : cwj
 * @describe : 家政人员工作时长记录
 * @date : 2020-5-21
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class HouseServiceWorkTimeVo {
    private Integer id;  //主键ID
    private Integer staffId; //家政人员Id
    private String openTime; // 开始工作时间
    private String endTime;  //结束工作时间
    private String orderNumber; //订单号
    private Integer reservationId ; //预约服务Id
    private String totalTime; //本次服务总时长
    private String status; //状态，用于结算工资。1：未结算 2：已结算
}
