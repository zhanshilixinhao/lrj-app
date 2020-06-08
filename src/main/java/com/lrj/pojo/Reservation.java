package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Lxh
 * @date 2020/4/23 16:34
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
public class Reservation{
    private static final long serialVersionUID = 5087055302184564912L;
    @Id
    private Integer reservationId;
    private Integer status;  //订单状态
    private Integer trackingStatus;  //订单追踪状态
    private Integer orderType; //订单类型
    private Integer grabOrderId; //抢单人ID
    private String address; //地址
    private String createTime; //创建时间
    private String updateTime; //更新时间
    private Integer userId; //用户ID
    private String orderNumber; //订单号
    private String longitude; //经度
    private String latitude; //纬度
    private String  reservationJson; // 预约的具体内容
    private Integer isUrgent; //是否加急
    private Integer isService; //是否收取服务费
    private BigDecimal totalPrice;//总价
    private String takeOrderTime;//抢单时间
    private String getClothesTime; //用户预计取衣时间
    private String reGetClothesTime;
    private String sendBackTime;
    private String isShare;  //是否是分享单
    private Integer is_end;
}
