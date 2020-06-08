package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : cwj
 * @describe : 用户红包
 * @date : 2020-4-22
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponVo {
    private Integer userId;// 用户ID
    private Integer id; // 自增主键
    private Integer denomination; // 红包金额
    private String createTime; //创建时间
    private String limitTime;// 过期时间
    private Integer active; //是否可用  0：不可用   1：可用
    private Integer source;//红包获得方式：1.后台发放红包，2.订单分享赠送，3.唤醒红包，4.邀请下单赠送
    private Integer couponType;//2衣物，3家居，4鞋子，5家政，1通用 6,月卡
    private String useInstructions; //使用说明
    private String orderNumber; //分享订单时的订单号
}
