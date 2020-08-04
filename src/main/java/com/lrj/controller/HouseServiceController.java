package com.lrj.controller;

import com.lrj.VO.*;
import com.lrj.service.IAreaManagementService;
import com.lrj.service.IHouseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : cwj
 * @describe : 家政服务
 * @date : 2020-4-9
 */
@RestController
public class HouseServiceController {
    @Resource
    private IHouseService houseService;
    @Resource
    private IAreaManagementService areaManagementService;

    @RequestMapping(value = "/getHouseService",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getHouseService(Integer itemCategoryId,HttpServletRequest request){
        Integer areaType = Integer.parseInt(request.getParameter("areaType"));
        /** 校验必须参数 **/
        if (itemCategoryId == 0 || itemCategoryId==null ||areaType==0 || areaType==null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        List<HouseServiceVo> houseServiceVoList = houseService.findHouseService(itemCategoryId,areaType);
        /** 获取请求地址 **/
        StringBuffer url = request.getRequestURL();
        /** 拼接 **/
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        /** 拼接可访问图片地址 **/
        for (HouseServiceVo houseServiceVo : houseServiceVoList) {
            /** 图片地址 **/
            houseServiceVo.setPicture(tempContextUrl + houseServiceVo.getPicture());
        }
        return new ResultVo("success", 0, "查询成功", houseServiceVoList);
    }

    @RequestMapping(value = "/getHouseServicePid",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getHouseServicePid(){
        List<HouserServicePidVo> houserServicePidVoList = houseService.findHouseServicePidList();
        return new ResultVo("SUCCESS", 0, "查询成功！", houserServicePidVoList);
    }

    /**
     *  定制家政服务
     */

    @RequestMapping(value = "/getCustomHouseService",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getCustomHouseService(HttpServletRequest request){
        Integer areaType = Integer.parseInt(request.getParameter("areaType"));
        /** 校验必须参数 **/
        if (areaType == 0 || areaType==null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        List<HouseServiceVo> customHouseServiceList = houseService.findCustomHouseService(areaType);
        return new ResultVo("SUCCESS", 0, "查询成功！", customHouseServiceList);
    }

    /**
     * @Description: 获取基础面积-价格
     * @Author: LxH
     * @Date: 2020/5/28 10:12
     */
    @RequestMapping(value = "/findBasisArea",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo  findBasisArea(){
        List<AreaManagement> areaManagementList = areaManagementService.findBasisArea();
        return new ResultVo("SUCCESS", 0, "面积列表查询成功！", areaManagementList);
    }
}
