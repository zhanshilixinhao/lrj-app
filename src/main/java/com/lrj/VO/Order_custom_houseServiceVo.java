package com.lrj.VO;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author : cwj
 * @describe : 定制家政服务订单
 * @date : 2020-4-17
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Order_custom_houseServiceVo extends OrderVo{
    private String serviceCycle;  //服务周期
    private Integer baseService; // 基础服务次数
    private BigDecimal baseServicePrice;//基础服务价格
    private String individualServiceJson; //个性服务以及次数
    private BigDecimal individualServicePrice; //个性服务价格
}
