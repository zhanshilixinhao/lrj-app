package com.lrj.VO;


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
public class UserMonthCardVo{
    private static final long serialVersionUID = -8840112769263719469L;
    private Integer appUserId;
    private String userPhone = "";
    private String userName = "";
    private Integer monthCardId; //用户月卡类型ID
    private Integer monthCatdCont;//用户月卡剩余使用次数
    private String startTime; //月卡开始使用时间
    private String endTime; //月卡结束时间
    private String itemJson;//月卡内剩余可洗衣物
}
