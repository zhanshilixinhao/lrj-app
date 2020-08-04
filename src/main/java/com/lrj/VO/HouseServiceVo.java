package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : cwj
 * @describe : 家政服务
 * @date : 2020-4-8
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class HouseServiceVo {

    private Integer id; //服务Id
    private Integer itemCategoryId; //导航栏Id
    private Integer type;  //类型：1：基础服务  2：个性服务
    private String houseName; //名称
    private String itemUnit;//单位
    private String include; //服务包含项目
    private Double price;  //价格
    private String serviceNote; //服务包含
    private String picture; //图片路径
    private Integer active; //状态 0：下架 1：上架
    private String acceptNorm; //服务标准
    private String serviceTime;//服务时长
    private Integer areaType;//面积类型
}
