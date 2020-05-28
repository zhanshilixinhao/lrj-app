package com.lrj.service;

import com.lrj.VO.BuyCardOptionVo;
import com.lrj.VO.FormerResult;
import com.lrj.VO.MonthCardWashingCountVo;
import com.lrj.pojo.MonthCard;

import java.util.List;
import java.util.Map;

/**
 * @author Lxh
 * @date 2020/4/16 11:49
 */
public interface IMonthCardService {
    List<MonthCard> getMonthCardList();

    /**
     * 获取月卡详情
     * @param id
     * @return
     */
    List<MonthCardWashingCountVo> getMonthCardWashingCountList(Integer id);

   /* FormerResult selectUserMonthCard(Integer userId);

    FormerResult buyCard(BuyCardOptionVo option);*/
}
