package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppItemVo {

    private Integer itemId;
    private Integer itemCategoryId;
    private String picture;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private String itemName;
    private String itemUnit;
    private String commodityExplain;
    private String duration;
    private Integer inShoppingCartCount;
}
