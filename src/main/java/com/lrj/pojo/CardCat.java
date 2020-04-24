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
@Table(name = "wx_month_card")
public class CardCat extends Base{
    private static final long serialVersionUID = 1393109529600812281L;
    @Id
    private Integer id;
    private Integer count;
    private String name;
    private BigDecimal price;
    private Integer expire;
    private Integer status;
    private Integer cardType;
    private Integer timeType;
    private String detail;
    private Float shareDicount;
}
