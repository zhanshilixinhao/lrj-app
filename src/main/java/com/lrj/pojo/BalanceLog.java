package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Lxh
 * @Date: 2020/6/30 16:47
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "balance_log")
public class BalanceLog extends Base{
    private static final long serialVersionUID = 4716300751410493277L;
    @Id
    private Integer balanceLogId;
    private Integer userId;
    private String type;
    private BigDecimal amount;
}
