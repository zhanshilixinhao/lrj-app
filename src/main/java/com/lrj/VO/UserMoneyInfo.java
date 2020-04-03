package com.lrj.VO;

import com.lrj.pojo.Balance;
import com.lrj.pojo.GivenBalance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * <b>项目名称</b>：lanrenxiyi<br>
 * <b>类名称</b>：UserMoneyInfo<br>
 * <b>类描述</b>：用户的余额、优惠券数量和赠送金额信息<br>
 * <b>修改备注</b>：<br>
 * 
 * @author WR<br>
 * @version
 * 
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserMoneyInfo {

    private BigDecimal balance;// 余额
    private Integer couponNum;// 优惠券数量
    @SuppressWarnings({})
    private List<GivenBalance> givenBalance;// 赠送金额信息
    private Integer Days; //赠送余额剩余天数


    public BigDecimal getGivenBalanceTotal() {

        BigDecimal total = new BigDecimal(0.00);
        for (GivenBalance temp : givenBalance) {
            total = total.add(temp.getGivenBalance());
        }
        return total;
    }

}
