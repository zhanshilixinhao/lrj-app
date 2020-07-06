package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author : cwj
 * @describe : 平台流水记录
 * @date : 2020-6-28
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PayOperation {
    private Integer id;
    private Integer tradeSource;
    private Integer tradeType;
    private String bankType;
    private BigDecimal totalFee;
    private String transactionId;
    private String outTradeNo;
    private String createTime;
    private Integer userId;
    private String reason;
    private Integer checkStatus;
    private String userPhone;
}
