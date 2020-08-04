package com.lrj.controller;

import com.lrj.VO.FormerResult;
import com.lrj.VO.OrderVo;
import com.lrj.VO.Result;
import com.lrj.VO.ResultVo;
import com.lrj.pojo.Activity;
import com.lrj.pojo.DistributionStation;
import com.lrj.service.IActivityService;
import com.lrj.service.IDistributionStationService;
import com.lrj.service.IOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : cwj
 * @describe : 配送站入口
 * @date : 2020-7-28
 */
@RestController
public class DistributionStationController {

    @Resource
    private IDistributionStationService distributionStationService;
    /**
     * 查询所有配送站
     */
    @RequestMapping(value = "/getDistriButionStationList",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getDistriButionStationList(){
        List<DistributionStation> distributionStationList = distributionStationService.getDistriButionStationList();
        return new ResultVo("SUCCESS", 0, "查询成功!", distributionStationList);
    }

}
