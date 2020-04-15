package com.lrj.controller;

import com.lrj.VO.HouseServiceVo;
import com.lrj.VO.ResultVo;
import com.lrj.service.IHouseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : cwj
 * @describe : 家政服务
 * @date : 2020-4-9
 */
public class HouseServiceController {


    @Resource
    private IHouseService houseService;

    @RequestMapping(value = "/getHouseService",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getHouseService(Integer typeId){
        /** 校验必须参数 **/
        if (typeId == null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        List<HouseServiceVo> houseServiceVoList = houseService.findHouseService(typeId);
        return new ResultVo("success", 0, "查询成功", houseServiceVoList);
    }
}
