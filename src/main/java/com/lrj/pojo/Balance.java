package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Lxh
 * @date 2020/4/1 16:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "balance")
public class Balance {
    @Id
    private Integer userId;
    private BigDecimal balance;
    private BigDecimal topUpAmount;
    private BigDecimal expendAmount;
    private String lastModifyTime;
    private String createTime;

}
