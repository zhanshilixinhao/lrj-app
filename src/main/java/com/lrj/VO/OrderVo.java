package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author : cwj
 * @describe : 订单流
 * @date : 2020-4-10
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo {

    private Integer appOrderId; //  订单号
    private Integer userId; // 用户Id
    private BigDecimal totalPrice; // 总价
    private Integer status; // 订单状态
    private Integer payStatus; //  支付状态
    private Integer takeAddress; // 取件地址
    private Integer receiveAddress; // 送件地址
    private Integer isLock; //  订单是否已抢
    private Integer traceStatus; //  订单当前追踪状态
    private String takeTime = ""; // 取件时间
    private String createTime = ""; // 生成时间
    private Integer orderType;          //订单类型  1.普通订单，2.月卡订单
    private Integer monthCardId; //月卡订单详情
}
