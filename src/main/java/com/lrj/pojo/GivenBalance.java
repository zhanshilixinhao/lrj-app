package com.lrj.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import	java.util.Date;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author Lxh
 * @date 2020/4/2 9:31
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "given_balance")
public class GivenBalance {
    private BigInteger orderId;
    @Id
    private Integer givenBalanceId; // 赠送金额ID
    private BigDecimal givenBalance; // 用户赠送余额
    private BigDecimal topupGivenAmount;
    private BigDecimal expendGivenAmount;
    private String startingTime = ""; // 有效期起始时间
    private String expirationTime = ""; // 有效期截止时间
    private Integer expired; // 是否过期：1：过期 0:没过期
    private Integer status; // 抵用券类型
    private String lastModifyTime ; // 赠送余额最后变动时间
    private String createTime;
    private Integer userId;
}
