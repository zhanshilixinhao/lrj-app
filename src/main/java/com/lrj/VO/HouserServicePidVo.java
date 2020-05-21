package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : cwj
 * @describe : 家政栏目（导航栏）
 * @date : 2020-5-18
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class HouserServicePidVo {
    private Integer itemCategoryId;
    private String categoryName;
    private String categoryPic;
    private Integer pid;
    private Integer isShow;
}
