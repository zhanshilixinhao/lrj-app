package com.lrj.mapper;

import com.lrj.VO.BuyCardOptionVo;
import com.lrj.VO.OrderAppointVo;
import com.lrj.VO.UserMonthCardVo;
import com.lrj.pojo.UserMonthCard;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * @author Lxh
 * @date 2020/4/16 15:11
 */
public interface UserMonthCardMapper extends Mapper<UserMonthCard>, MySqlMapper<UserMonthCard> {
    UserMonthCardVo selectUserMonthCard(Integer userId);

    List<OrderAppointVo> selectOrderList(BuyCardOptionVo option);
}
