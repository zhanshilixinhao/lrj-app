package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppItemVo {

    private Integer itemId;
    private Integer itemCategoryId;
    private String picture;
    private BigDecimal originalPrice;
    private BigDecimal price;  //现价
    private String itemName;
    private String itemUnit;
    private String commodityExplain;
    private String duration;
    private Integer inShoppingCartCount;
    private BigDecimal promotionOriginalCost; //限时特价的原价
    private Date promotionEndDate;//结束时间 Date类型
    private long promotionEndDateLong; //结束时间 long
}
