package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import sun.dc.pr.PRError;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Description: 流水记录对象
 * @Author： Lxh
 * @Date： 2020/6/24 9:10
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pay_operation")
public class PayOperation extends Base{
    private static final long serialVersionUID = 5963214818070481775L;

    @Id
    private Integer id;
    /**支付平台 1：余额 2：微信支付  3：支付宝支付*/
    private Integer tradeSource;
    /**交易类型 1：支付   -1：提现*/
    private Integer tradeType;

    private String bankType;

    private BigDecimal totalFee;
    /**流水号*/
    private String transactionId;
    /**订单号*/
    private String outTradeNo;

    private Integer userId;
    /**退款原因*/
    private String reason;

    private Integer checkStatus;

    private String userPhone;
}
