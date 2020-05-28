package com.lrj.controller;

import com.lrj.VO.MonthCardWashingCountVo;
import com.lrj.VO.ResultVo;
import com.lrj.pojo.MonthCard;
import com.lrj.service.IMonthCardService;
import net.sf.json.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        @RequestMapping(value = "/getMonthCardList",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getMonthCardList(){
        List<MonthCard> monthCardList = monthCardService.getMonthCardList();
        /*//处理了反斜杠问题
        for(MonthCard monthCard : monthCardList){
            //处理了反斜杠问题
            JSONArray provinceArray = JSONArray.fromObject(monthCard.getItemJson());
            List<Map<String, Object>> provinceListJson = (List<Map<String, Object>>) provinceArray;
            monthCard.setItemJsonList(provinceListJson);
        }*/
        //拼接月卡具体详情
        for(MonthCard monthCard : monthCardList){
            List<MonthCardWashingCountVo> monthCardDetail = monthCardService.getMonthCardWashingCountList(monthCard.getCardId());
            monthCard.setMonthCardWashingCountList(monthCardDetail);
        }
        return new ResultVo("SUCCESS", 0, "查询成功", monthCardList);
    }
}


