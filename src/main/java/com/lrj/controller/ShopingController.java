package com.lrj.controller;

import com.lrj.VO.AppShoppingVo;
import com.lrj.VO.ItemCategoryVo;
import com.lrj.VO.ResultVo;
import com.lrj.VO.ValueAddedServicesVo;
import com.lrj.service.IItemCategoryService;
import com.lrj.service.IShoppingService;
import com.lrj.util.DateUtils;
import com.lrj.util.MessagesUtil;
import com.mysql.cj.Messages;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author : cwj
 * @describe : 购物车
 * @date : 2020-4-1
 */
@RestController
public class ShopingController {

    @Resource
    private IShoppingService shoppingService;
    @Resource
    private ValueAddedServicesController valueAddedServicesController;

    @RequestMapping(value = "/getShoppingDetails",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getItemCategory(Integer userId, HttpServletRequest request){
        /** 校验必须参数 **/
        if (userId == null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        /** 获取购物车商品列表 **/
        List<AppShoppingVo> appShoppingVoList = shoppingService.getShoppingDetails(userId);
        /** 获取请求URL **/
        StringBuffer url = request.getRequestURL();
        /** 截取拼接URL **/
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString()
                + "/" + MessagesUtil.getString("virtual_directory") + "/";
        /** 获取当天日期 **/
        Date now = DateUtils.formatStringToDate(DateUtils.formateDateWithoutTime(new Date()), "yyyy-MM-dd");
        /** 图片地址拼接及价格计算 **/
        for (AppShoppingVo appShoppingVo : appShoppingVoList) {
            /** 拼接地址 **/
            appShoppingVo.setPicture(tempContextUrl + appShoppingVo.getPicture());
            /** 获取增值服务 **/
            ResultVo result2 = valueAddedServicesController.listValueAddedServices(new ResultVo(), appShoppingVo.getItemId());
            /** 设置 **/
            appShoppingVo.setValueAddedServicesVos(result2.getData());
        }
        return new ResultVo("success",0,"查询成功",appShoppingVoList);
    }

}
