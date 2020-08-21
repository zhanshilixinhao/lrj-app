package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * @author : cwj
 * @describe : 商品集合  ：用来装载用户下单的订单json数据
 * @date : 2020-8-19
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemJSON {
    @Id
    private Integer id;
    private Integer reservationId;
    private String orderNumber;
    private Integer itemId;
    private Integer quentity;
    private String itemName;
    private String picture;
    private BigDecimal price;
    private String itemUnit;
}
