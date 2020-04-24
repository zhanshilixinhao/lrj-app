package com.lrj.VO;

import com.lrj.pojo.CardCat;
import com.lrj.pojo.UserMonthCard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lxh
 * @date 2020/4/16 15:28
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserMonthCardVo extends UserMonthCard {
    private static final long serialVersionUID = -8840112769263719469L;
    private Byte cardTimeType;

    private Integer remainCount;
}
