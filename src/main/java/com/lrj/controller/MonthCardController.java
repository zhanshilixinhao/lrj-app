package com.lrj.controller;

import com.lrj.VO.BuyCardOptionVo;
import com.lrj.VO.FormerResult;

import com.lrj.VO.ResultVo;
import com.lrj.pojo.MonthCard;
import com.lrj.service.IMonthCardService;
import com.lrj.util.CommonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author Lxh
 * @date 2020/4/16 11:35
 */
@RestController
public class MonthCardController {
    @Resource
    private IMonthCardService monthCardService;

    /**
     * 获取微信月卡列表
     *
     * @return FormerResult
     */
    @RequestMapping("/getMonthCardList")
    public ResultVo getMonthCardList(){
        List<MonthCard> monthCardList = monthCardService.getMonthCardList();
        return new ResultVo("SUCCESS", 0, "查询成功", monthCardList);
    }

/*    *//**
     * 购买月卡
     *
     * @return FormerResult
     *//*
    @RequestMapping("/buyCard")
    public FormerResult buyCard(BuyCardOptionVo option){
        return monthCardService.buyCard(option);
    }*/
}


