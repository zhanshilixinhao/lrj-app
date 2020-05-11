/*
package com.lrj.controller;

import com.lrj.VO.FormerResult;
import com.lrj.VO.StaffInfoVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

*/
/**
 * @author : cwj
 * @describe : 懒人家助手 员工控制层
 * @date : 2020-5-8
 *//*

public class StaffController {

    @Resource
    private IStaffService staffService;
    */
/**
     * 查询员工信息
     *//*

    @RequestMapping(value = "/getStaffInfoByStaffId",method = {RequestMethod.GET, RequestMethod.POST})
    public FormerResult getStaffInfoByStaffId(Integer staffId){
        StaffInfoVo staffInfoVo = staffService.getStaffInfoByStaffId(staffId);
        return new FormerResult("SUCCESS",0,"查询成功",staffInfoVo);
    }
}
*/
