package com.lrj.service;

import com.lrj.VO.BuyCardOptionVo;
import com.lrj.VO.FormerResult;

/**
 * @author Lxh
 * @date 2020/4/16 11:49
 */
public interface IMonthCardService {
    FormerResult getCardCatTypeList(Integer type);

    FormerResult getAllCardCatList();

    FormerResult selectUserMonthCard(Integer userId);

    FormerResult buyCard(BuyCardOptionVo option);
}
