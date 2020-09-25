package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author : cwj
 * @describe : 员工受益记录
 * @date : 2020-6-7
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class StaffRebateVo {
    private Integer id;//主键
    private Integer staffId;
    private BigDecimal backMoney;
    private String createTime;
    private Integer reservationId; //与收益有关的预约单号
}
