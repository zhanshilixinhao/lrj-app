package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : cwj
 * @describe : 后台制定销售商家可销售的订单内容
 * @date : 2020-7-8
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class MerchantOrder {

 private Integer merchantOrderId;
 private Integer orderType;
 private BigDecimal totalPrice; //实际支付金
 private BigDecimal originalPrice;//原价金额
 private BigDecimal activityPrice; //活动减免金额
 private String detailJson; //除月卡外，其余订单类型商品数据放置在这里
 private Date createTime;
 private Integer active;
 private Integer monthCardId;//月卡Id
 private Integer serviceCycle;//服务周期
 private Integer baseServiceCount;//基础服务次数
 private Integer workTime;//工作时间
 private String houseArea;//住宅面积
 private BigDecimal baseServicePrice;//基础服务价格
}
