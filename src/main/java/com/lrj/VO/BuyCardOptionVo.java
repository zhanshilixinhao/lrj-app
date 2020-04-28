package com.lrj.VO;

import com.lrj.pojo.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lxh
 * @date 2020/4/16 17:50
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class BuyCardOptionVo extends PageParam {
    private static final long serialVersionUID = -2057096241432618812L;
    private Integer userId; // 购买用户id
    private Integer wxMonthCardId; // 购买月卡id
    private Byte isShare; // 是否分享了
    private Integer addressId;
    private Integer status;
    private Integer isAuto;
    private Long orderNo;
    private String optionTime; // 上门时间
    private Integer payType;// 支付方式 1,小程序 2 APP

}
