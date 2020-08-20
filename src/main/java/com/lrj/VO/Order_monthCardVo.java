package com.lrj.VO;

import com.lrj.pojo.ItemJSON;
import com.lrj.pojo.MonthCard;
import com.lrj.util.BigDecimalUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.sf.json.JSONArray;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 月卡订单
 * @date : 时间
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Order_monthCardVo extends OrderVo {
    private Integer id;
    private String orderNumber;
    private Integer userId;
    private String userheadPhoto; //用户头像
    private Integer active; //用户月卡状态
    private Integer monthCardId;//月卡类型Id
    private String monthCardName;//月卡名称
    private Integer userMonthCardCount;//月卡剩余可使用次数
    private List<ItemJSON> userMonthCardItemJSONArray;//剩余可洗内容
    private String createTime;//月卡创建时间
    private String endTime; //月卡结束时间
}
