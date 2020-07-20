package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author Lxh
 * @Date 2020/7/6 10:10
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_rebate")
public class AppRebate extends Base {
    private static final long serialVersionUID = 4374800507222817397L;
    @javax.persistence.Id
    private Integer Id;
    private Integer userId;
    private Integer lowId;
    private BigDecimal backMoney;
    private String orderNumber;
    private String source;
    /**
     * 1 用户 2. 商家
     */
    private Integer type;
}
