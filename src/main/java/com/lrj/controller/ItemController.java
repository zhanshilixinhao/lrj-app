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
import java.util.ArrayList;
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
    public ResultVo getItemList(Integer itemCategoryId,Integer page,Integer rows,Integer userId, HttpServletRequest request){
        /** 校验必须参数 **/
        if (itemCategoryId == null || page == null || rows == null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        /** 计算分页索引 **/
        int start = (page - 1) * rows;
        /** 参数MAP **/
        Map<String, Integer> paramMap = new HashMap<String, Integer>();
        paramMap.put("start", start); // 开始条数
        paramMap.put("rows", rows); // 每页要显示多少条
        paramMap.put("categoryId", itemCategoryId); // 商品类表id
        /** 用户id **/
        paramMap.put("userId", userId);
        /** 查询商品列表 **/
        List<AppItemVo> appItemVoList = itemService.getItemListByCategoryId(paramMap);
        /** 获取请求地址 **/
        StringBuffer url = request.getRequestURL();
        /** 拼接 **/
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString() + "/";
        /** 拼接可访问图片地址 **/
        for (AppItemVo appItemVo : appItemVoList) {
            /** 图片地址 **/
            appItemVo.setPicture(tempContextUrl + appItemVo.getPicture());
            /** 原价 **/
            appItemVo.setOriginalPrice(appItemVo.getPrice());
        }
        return new ResultVo("success",0,"查询成功",appItemVoList);
    }

    /**
     *  查询限时特价 商品
     * @return
     */
    @RequestMapping(value = "/getSpecialItem",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getSpecialItem(HttpServletRequest request){
        List<AppItemVo> specialItemList = new ArrayList<>();
        specialItemList = itemService.getSpecialItem();
        /** 获取请求地址 **/
        StringBuffer url = request.getRequestURL();
        /** 拼接 **/
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        /** 拼接可访问图片地址 **/
        for (AppItemVo appItemVo : specialItemList) {
            /** 图片地址 **/
            appItemVo.setPicture(tempContextUrl + appItemVo.getPicture());
            /**截止时间**/
            appItemVo.setPromotionEndDateLong(appItemVo.getPromotionEndDate().getTime());
        }
        return new ResultVo("SUCCESS", 0, "查询成功！", specialItemList);
    }

    /*@RequestMapping(value = "/updateImagesUrl",method = {RequestMethod.GET,RequestMethod.POST})
    public void updateImagesUrl(){
        *//** 参数MAP **//*
        Map<String, Integer> paramMap = new HashMap<String, Integer>();
        paramMap.put("start", 0); // 开始条数
        paramMap.put("rows", 500); // 每页要显示多少条
        paramMap.put("categoryId", 11); // 商品类表id
        List<AppItemVo> appItemVoList = itemService.getItemList();
        for (AppItemVo appItemVo : appItemVoList){
            String picture = appItemVo.getPicture();
            picture = picture.replaceAll("\\\\", "/");
            itemService.updateImagesUrl(appItemVo.getAppItemId(),picture);
        }
    }*/

}
