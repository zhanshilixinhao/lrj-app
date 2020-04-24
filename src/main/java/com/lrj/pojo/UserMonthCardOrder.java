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
 * @date 2020/4/17 10:09
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_month_card_order")
public class UserMonthCardOrder extends Base{
    private static final long serialVersionUID = -6876280182180806574L;
    @Id
    private Integer id;
    private Integer userId;
    private Integer wxMonthCardId;
    private BigDecimal price;
    private Long orderNo;
    private Byte status;
    private String optionTime;
    private Date startTime;
    private Date endTime;
    private Date payTime;
    private Byte payWay;
    private Integer addressId;
    private Long orderId;
}
