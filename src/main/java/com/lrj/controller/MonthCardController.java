package com.lrj.controller;

import com.lrj.VO.BuyCardOptionVo;
import com.lrj.VO.FormerResult;

import com.lrj.service.IMonthCardService;
import com.lrj.util.CommonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author Lxh
 * @date 2020/4/16 11:35
 */
@RestController
@RequestMapping("wx/monthcard")
public class MonthCardController {
    @Resource
    private IMonthCardService monthCardService;

    /**
     * 获取微信月卡列表
     *
     * @return
     */
    @RequestMapping("/typeList")
    public FormerResult getCardCatTypeList(Integer type){
        if (type==null) {
            return CommonUtil.FAIL(new FormerResult(),"参数不能为空",null);
        }
        return monthCardService.getCardCatTypeList(type);
    }
    /**
     * 获取微信月卡列表
     *
     * @return
     */
    @RequestMapping("/allList")
    @ResponseBody
    public FormerResult getAllCardCatList(){
        return monthCardService.getAllCardCatList();
    }
    /**
     * 获取用户的月卡
     *
     * @return
     */
    @RequestMapping("/userMonthCard")
    @ResponseBody
    public FormerResult getUserMonthCard(Integer userId) {
        return monthCardService.selectUserMonthCard(userId);
    }

    /**
     * 购买月卡
     *
     * @return
     */
    @RequestMapping("/buyCard")
    @ResponseBody
    public FormerResult buyCard(BuyCardOptionVo option){
        return monthCardService.buyCard(option);
    }
}


