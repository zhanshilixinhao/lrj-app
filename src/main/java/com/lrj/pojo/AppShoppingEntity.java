package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author : cwj
 * @describe : 购物车-数据库实列
 * @date : 2020-6-16
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppShoppingEntity {
    private Integer appShoppingId; //
    private Integer userId; //
    private Integer itemId; //
    private Integer quantity; //
    private Integer supportValue; //
    private Integer isPouch; //
    private Integer supportScope; //
    private BigDecimal supportMoney; //
    private Integer first;
}
