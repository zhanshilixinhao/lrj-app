package com.lrj.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author fl
 * @descrip: 返利
 * @date 2020/6/12 0012下午 1:52
 */
@Data
@TableName("app_rebate")
@Accessors(chain = true)
public class Rebate {
    @TableId(type = IdType.AUTO)
    private Integer Id;
    private Integer userId;
    private Integer lowId;
    private BigDecimal backMoney;
    private LocalDateTime createTime;
    /**
     * 1 用户 2. 商家
     */
    private Integer type;

    public static final String CREATE_TIME = "create_time";
    public static final String USER_ID_COLUMN = "user_id";
}
