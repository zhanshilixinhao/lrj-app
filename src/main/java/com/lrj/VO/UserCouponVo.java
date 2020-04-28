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
    private Integer couponId; // 自增主键
    private Integer denomination; // 红包金额
    private Integer sysCouponId;// 属于系统哪种红包
    private String createTime; //创建时间
    private String limitTime;// 过期时间
    private Integer active; //是否可用  0：不可用   1：可用
    private Integer isUsed;  //是否使用  0：未使用   1：已使用
    private String couponType;// 红包类型
    private String useInstructions; //使用说明
    private String type;//1衣物，2家居，3鞋子，4家政，5通用
}
