package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author : cwj
 * @describe : 用户 收益记录
 * @date : 2020-6-7
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserRebateVo {
    private Integer id;//主键
    private Integer userId;
    private String userName; //用户名
    private Integer low_id; //下级Id
    private String lowUserName; //下级用户名
    private BigDecimal backMoney;
    private String createTime;
    private Integer userType;  //1.用户 2.商家
    private String source; //资金来源
    private String orderNumber; //与返利有关的订单号
}
