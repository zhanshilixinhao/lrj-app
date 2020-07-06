package com.lrj.pojo;

import com.alipay.api.domain.DataEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author : cwj
 * @describe : 用户消费记录
 * @date : 2020-6-30
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "balance_log")
public class BalanceRecord {
    private Integer balanceLogId;
    private Integer userId;
    private Integer type; //记录类型:默认0(无意义) 1.充值 2.消费 3.退款(预留)
    private BigDecimal amount;
    private String createTime;
    private String transactionId; //微信支付成功（微信订单号）
    private Integer status;//   流水状态 0：未成功   1：成功
    private String rechargeOrderNumber; // 充值订单号


}
