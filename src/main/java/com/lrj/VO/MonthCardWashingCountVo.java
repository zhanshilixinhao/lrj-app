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
    private String itemCategoryName;
    private Integer itemCategoryCount;
}
