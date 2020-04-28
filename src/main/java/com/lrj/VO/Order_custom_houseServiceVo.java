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
    private String orderNumber; //订单号
    private String openTime;//服务开始时间
    private String endTime; //服务结束时间
    private Integer baseServiceCount; // 每月基础服务次数（剩余）
    private BigDecimal baseServicePrice;//基础服务价格
    private String individualServiceJson; //个性服务以及次数
    private BigDecimal individualServicePrice; //个性服务价格
}
