package com.lrj.controller;

import com.lrj.VO.Result;
import com.lrj.VO.ShoppingVo;
import com.lrj.VO.ResultVo;
import com.lrj.service.IShoppingService;
import com.lrj.util.DateUtils;
import com.lrj.util.MessagesUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    /**
     * 获取购物车内容
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping(value = "/getShoppingDetails",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getItemCategory(Integer userId, HttpServletRequest request){
        /** 校验必须参数 **/
        if (userId == null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        /** 获取购物车商品列表 **/
        List<ShoppingVo> shoppingVoList = shoppingService.getShoppingDetails(userId);
        /** 获取请求URL **/
        StringBuffer url = new StringBuffer();
        url.append("http://www.51lrj.com/getItemList");
        /** 拼接 **/
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString()
                + "/" + MessagesUtil.getString("virtual_directory") + "/";
        /** 获取当天日期 **/
        Date now = DateUtils.formatStringToDate(DateUtils.formateDateWithoutTime(new Date()), "yyyy-MM-dd");
        /** 图片地址拼接及价格计算 **/
        for (ShoppingVo shoppingVo : shoppingVoList) {
            /** 拼接地址 **/
            shoppingVo.setPicture(tempContextUrl + shoppingVo.getPicture());
            /** 获取增值服务 **/
            ResultVo result2 = valueAddedServicesController.listValueAddedServices(new ResultVo(), shoppingVo.getItemId());
            /** 设置 **/
            shoppingVo.setValueAddedServicesVos(result2.getData());
        }
        return new ResultVo("success",0,"查询成功", shoppingVoList);
    }

    /**
     * 添加购物车
     * @param userId
     * @param itemId
     * @param isPouch
     * @return
     */
    @RequestMapping(value = "/addShopping",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo addShopping(Integer userId, String itemId, Integer isPouch) {

        /**效验必须参数**/
        if (userId == null || itemId == null || isPouch == null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        /** 参数map **/
        Map<String, Integer> paramMap = new HashMap<String, Integer>();
        /** 用户id **/
        paramMap.put("userId", userId);

        /** 判断是对个还是单个 **/
        if (itemId.contains(",")) {
            /** 分割 **/
            String[] itemIds = itemId.split(",");
            /** 遍历 **/
            for (String s : itemIds) {
                /** 商品ID **/
                int itemIdInt = Integer.parseInt(s);
                paramMap.put("itemId", itemIdInt);
                /** 遍历 添加多个 **/
                /** 查询是否已添加该商品 **/
                List<ShoppingVo> data = shoppingService.findUserShopping(paramMap);
                /** 如果存在则更新数量 **/
                if (data != null && data.size() > 0) {
                    shoppingService.updateUserShopping(paramMap);
                }
                else {
                    /** 否则重新创建 **/
                    ShoppingVo shoppingVo = new ShoppingVo();
                    shoppingVo.setUserId(userId);
                    shoppingVo.setItemId(itemIdInt);
                    shoppingVo.setQuantity(1);
                    shoppingVo.setSupportValue(0);
                    shoppingVo.setIsPouch(isPouch);
                    shoppingVo.setFirst(0);
                    shoppingService.insertAppShopping(shoppingVo);
                }
            }
        }
        else {
            /** 查询是否已添加该商品 **/
            List<ShoppingVo> data = shoppingService.findUserShopping(paramMap);
            /** 如果存在则更新数量 **/
            if (data != null && data.size() > 0) {
                shoppingService.updateUserShopping(paramMap);
            }
            else {
                /** 否则重新创建 **/
                ShoppingVo shoppingVo = new ShoppingVo();
                shoppingVo.setUserId(userId);
                shoppingVo.setItemId(Integer.parseInt(itemId));
                shoppingVo.setQuantity(1);
                shoppingVo.setSupportValue(0);
                shoppingVo.setIsPouch(isPouch);
                shoppingVo.setFirst(0);
                shoppingService.insertAppShopping(shoppingVo);
            }
        }
        return new ResultVo("success", 0, "成功",null);
    }


    /**
     * 购物车商品数量保存
     * @param userId
     * @param itemId
     * @param quantity
     * @param req
     * @return
     */
    @RequestMapping(value = "/saveShoppingQuantity",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo saveShoppingQuantity(Integer userId, Integer itemId, Integer quantity, HttpServletRequest req) {

        if (userId == null || itemId == null || quantity == null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        Map<String, Integer> paramMap = new HashMap<String, Integer>();
        paramMap.put("userId", userId);
        paramMap.put("itemId", itemId);
        paramMap.put("quantity", quantity);
        shoppingService.updateUserShoppingQuantity(paramMap);
        return new ResultVo("success", 0, "成功",null);
    }

    /**
     * 滑动删除购物车里的商品(批量删除)
     */
    @RequestMapping(value = "/removeShoppingItem",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo removeShoppingItem(Integer shoppingId) {

        if (shoppingId == null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        shoppingService.deleteShoppingId(shoppingId);
        return new ResultVo("success", 0, "成功",null);
    }

    /**
     * 清空购物车
     */
    @RequestMapping(value = "/emptyShopCart", method = RequestMethod.POST)
    public ResultVo emptyShopCart(Integer userId) {
        /** 校验必须字段 **/
        if (userId == null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        /** 清空购物车 **/
        shoppingService.emptyShopCart(userId);
        /** 返回结果 **/
        return new ResultVo("success", 0, "参数有误,请检查参数",null);
    }
}
