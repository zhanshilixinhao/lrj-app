package com.lrj.controller;

import com.lrj.VO.FormerResult;
import com.lrj.VO.ResultVo;
import com.lrj.pojo.Consignee;
import com.lrj.service.IConsigneeService;
import com.lrj.util.CommonUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @author Lxh
 * @date 2020/4/15 15:06
 *
 */
@RestController
public class ConsigneeController {
    @Resource
    private IConsigneeService consigneeService;
    //查询用户地址
        @RequestMapping("/getUserAddressList")
    public FormerResult getUserAddressList(Integer userId){
        if (userId==null||userId==0) {
            return CommonUtil.FAIL(new FormerResult(),"参数不能为空",null);
        }
        return consigneeService.getUserAddressList(userId);
    }

    //添加用户地址
    @RequestMapping("/createConsignee")
    public FormerResult createConsignee(Consignee consignee){
        /** 校验必须参数 **/
        if (consignee == null) {
            return new FormerResult("success", 1, "参数有误,请检查参数",null);
        }
        return consigneeService.countUserAddress(consignee);
    }

    /**
     * 删除用户地址
     */
    @RequestMapping(value = "/removeConsignee",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult removeConsignee(Integer appConsigneeId){
        /** 校验必须参数 **/
        if (appConsigneeId == null) {
            return new FormerResult("success", 1, "参数有误,请检查参数",null);
        }
        consigneeService.removeConsignee(appConsigneeId);
        return new FormerResult("success", 0, "删除成功！", null);
    }

    /**
     * 设置地址默认
     */
    @RequestMapping(value = "/updateConsigneeDefault",method = {RequestMethod.GET,RequestMethod.POST})
    public FormerResult updateConsigneeDefault(Integer userId,Integer appConsigneeId){
        /** 校验必须参数 **/
        if (userId == null || appConsigneeId==null) {
            return new FormerResult("success", 1, "参数有误,请检查参数",null);
        }
         consigneeService.updateConsigneeDefault(userId,appConsigneeId);
        return new FormerResult("success", 0, "修改完成",null);
    }
}
