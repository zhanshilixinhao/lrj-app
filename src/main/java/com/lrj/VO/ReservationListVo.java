package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author : cwj
 * @describe : 预约服务简单列表
 * @date : 2020-6-28
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReservationListVo {
    private Integer reserVationId; //服务号
    private String orderNumber; //订单号
    private Integer status; //状态
    private String picture; //图片
    private Integer count; //数量
    private String unit; //单位
    private BigDecimal totalPrice;//合计总价
}
