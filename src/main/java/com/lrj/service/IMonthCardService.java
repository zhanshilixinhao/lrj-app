package com.lrj.service;

import com.lrj.VO.BuyCardOptionVo;
import com.lrj.VO.FormerResult;
import com.lrj.pojo.MonthCard;

import java.util.List;

/**
 * @author Lxh
 * @date 2020/4/16 11:49
 */
public interface IMonthCardService {
    List<MonthCard> getMonthCardList();

   /* FormerResult selectUserMonthCard(Integer userId);

    FormerResult buyCard(BuyCardOptionVo option);*/
}
