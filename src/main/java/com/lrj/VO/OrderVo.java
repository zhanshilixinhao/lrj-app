package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import sun.security.provider.PolicySpiFile;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : cwj
 * @describe : 订单流
 * @date : 2020-4-10
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo {
    private Integer id;//主键
    private String orderNumber; //  订单号
    private String shareOrderNumber;//活动分享的订单号
    private Integer userId; // 用户Id
    private BigDecimal totalPrice; // 实际支付价格
    private BigDecimal originalPrice;//原价金额
    private BigDecimal activityPrice; //活动减免金额
    private BigDecimal levelPrice;//等级减免金额
    private Integer activity; //参与的活动Id

    private Integer status; // 订单状态 订单状态 0未完成 1已完成 2：待付款 3：已付款 4：待评价
    private Integer payStatus; //  支付状态
    private String createTime = ""; // 生成时间
    private Integer userCouponId = 0; //红包Id
    private Integer orderType;       //订单类型  1.洗衣订单 2.洗衣月卡订单  3.单项家政服务  4.定制家政服务
    private String userPhone; //用户电话
    private Integer isShare;//是否已分享：被领取了

    //销售商家信息
    private Integer merchantId;//商家Id

    public static final int WASHING = 1;
    public static final int MONTH_WASHING = 2;
    public static final int HOUSE = 3;
    public static final int CUSTOM_HOUSE =4;


}
