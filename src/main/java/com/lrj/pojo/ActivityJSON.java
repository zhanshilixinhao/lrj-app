package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * @author : cwj
 * @describe : 商品集合  ：用来装载活动包含的商品数据
 * @date : 2020-9-1
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ActivityJSON {
    @Id
    private Integer id;
    private Integer itemId;
    private Integer quentity;
    private String itemName;
    private String picture;
    private BigDecimal price;
    private String itemUnit;
    private Integer activityId;
}
