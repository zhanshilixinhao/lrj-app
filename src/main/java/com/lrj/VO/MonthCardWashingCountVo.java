package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : cwj
 * @describe : 月卡套餐中洗涤的项目和次数
 * @date : 2020-5-26
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class MonthCardWashingCountVo {
    private Integer itemId;
    private String itemCategoryName;
    private Integer count;
    private Integer washingType; //洗衣类型 1：基础服务   2：个性服务
    private String itemUnit;//单位
}
