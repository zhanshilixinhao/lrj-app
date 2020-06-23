package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Lxh
 * @date 2020/4/17 11:03
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`order`")
public class Order extends Base {

    private static final long serialVersionUID = 5758430596298966910L;
    @Id
    private Long id;
    private String orderNumber;
    private Integer userId;
    private BigDecimal totalPrice;
    private Integer status;
    private Integer payStatus;
    private Integer orderType;
    private Integer userCouponId;
    private Integer isShare;
    private Integer deleted;
    private String traceStatus;
    private String userPhone;

    public static final String ORDER_NUMBER = "orderNumber";
}
