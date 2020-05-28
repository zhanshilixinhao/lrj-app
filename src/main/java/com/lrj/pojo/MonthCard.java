package com.lrj.pojo;

import com.lrj.VO.MonthCardWashingCountVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Lxh
 * @date 2020/4/16 10:58
 */

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class MonthCard {
    private static final long serialVersionUID = 1393109529600812281L;
    private Integer cardId;
    private Integer count;
    private String name;
    private BigDecimal price;
    private Integer status;
    private Float shareDicount;
    private String createTime;
    private String updateTime;
    private String notice; //月卡使用注意事项
    private List<MonthCardWashingCountVo> MonthCardWashingCountList; //月卡可洗具体内容
}
