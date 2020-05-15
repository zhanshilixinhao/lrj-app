package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Lxh
 * @date 2020/4/16 10:58
 */

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class MonthCard {
    private static final long serialVersionUID = 1393109529600812281L;
    @Id
    private Integer id;
    private Integer count;
    private String name;
    private BigDecimal price;
    private Integer status;
    private String detail;
    private Float shareDicount;
    private String createTime;
    private String updateTime;
    private String itemJson;
}
