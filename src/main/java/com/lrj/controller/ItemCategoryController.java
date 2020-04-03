package com.lrj.controller;

import com.lrj.VO.ItemCategoryVo;
import com.lrj.VO.ResultVo;
import com.lrj.service.IItemCategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : cwj
 * @describe : 商品类别
 * @date : 2020-4-1
 */
@RestController
public class ItemCategoryController {

    @Resource
    private IItemCategoryService itemCategoryService;

    @RequestMapping(value = "/getItemCategory",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getItemCategory(Integer pid){
        /** 校验必须参数 **/
        if (pid == null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        List<ItemCategoryVo> itemCategoryVoList = itemCategoryService.getItemCategory(pid);
        return new ResultVo("success",0,"查询成功",itemCategoryVoList);
    }

}
