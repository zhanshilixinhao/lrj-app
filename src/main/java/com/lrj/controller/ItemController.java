package com.lrj.controller;

import com.lrj.VO.AppItemVo;
import com.lrj.VO.ResultVo;
import com.lrj.service.IItemService;
import com.lrj.util.MessagesUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 单件商品
 * @date : 2020-4-1
 */
@RestController
public class ItemController {

    @Resource
    private IItemService itemService;

    @RequestMapping(value = "/getItemList",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getItemList(Integer categoryId,Integer page,Integer rows,Integer userId, HttpServletRequest request){
        /** 校验必须参数 **/
        if (categoryId == null || page == null || rows == null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        /** 计算分页索引 **/
        int start = (page - 1) * rows;
        /** 参数MAP **/
        Map<String, Integer> paramMap = new HashMap<String, Integer>();
        paramMap.put("start", start); // 开始条数
        paramMap.put("rows", rows); // 每页要显示多少条
        paramMap.put("categoryId", categoryId); // 商品类表id
        /** 用户id **/
        paramMap.put("userId", userId);
        /** 查询商品列表 **/
        List<AppItemVo> appItemVoList = itemService.getItemListByCategoryId(paramMap);
        /** 获取请求地址 **/ //request.getRequestURL();
        StringBuffer url = new StringBuffer();
        url.append("http://www.51lrj.com/getItemList");
        /** 拼接 **/
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString() + "/";
        /** 获取虚拟目录 **/
        String directory = MessagesUtil.getString("virtual_directory") + "/";
        /** 拼接可访问图片地址 **/
        for (AppItemVo appItemVo : appItemVoList) {
            /** 图片地址 **/
            appItemVo.setPicture(tempContextUrl + directory + appItemVo.getPicture());
            /** 原价 **/
            appItemVo.setOriginalPrice(appItemVo.getPrice());
        }
        return new ResultVo("success",0,"查询成功",appItemVoList);
    }
}
