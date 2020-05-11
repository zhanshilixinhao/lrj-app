package com.lrj.controller;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.HouseServiceVo;
import com.lrj.VO.ResultVo;
import com.lrj.service.IHouseService;
import com.lrj.util.MessagesUtil;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(value = "/getHouseService",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getHouseService(Integer pid,HttpServletRequest request){
        /** 校验必须参数 **/
        if (pid == 0 || pid==null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        List<HouseServiceVo> houseServiceVoList = houseService.findHouseService(pid);
        /** 获取请求地址 **/ //request.getRequestURL();
        StringBuffer url = new StringBuffer();
        url.append("http://192.168.0.103:8080/getHouseService");
        /** 拼接 **/
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString() + "/";
        /** 获取虚拟目录 **/
       /* String directory = MessagesUtil.getString("virtual_directory") + "/";*/
       String directory = "F:\\java\\swkjProject\\myfile";
        /** 拼接可访问图片地址 **/
        for (HouseServiceVo houseServiceVo : houseServiceVoList) {
            /** 图片地址 **/
            houseServiceVo.setImg(tempContextUrl + directory + houseServiceVo.getImg());
        }
        return new ResultVo("success", 0, "查询成功", houseServiceVoList);
    }

    @RequestMapping(value = "/customHouseService",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo customHouseService(HttpServletRequest request){
        request.getParameterValues("");
        return null;
    }
}
