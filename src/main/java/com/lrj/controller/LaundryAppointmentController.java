package com.lrj.controller;

import com.lrj.VO.BuyCardOptionVo;
import com.lrj.VO.FormerResult;
import com.lrj.mapper.UserMonthCardMapper;
import com.lrj.pojo.PageParam;
import com.lrj.service.LaundryAppointmentService;
import com.lrj.util.CommonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;

/**
 * @author Lxh
 * @date 2020/4/17 16:43
 */
@RestController
public class LaundryAppointmentController {
    @Resource
    private LaundryAppointmentService laundryAppointmentService;

    /**
     * 预约洗衣
     *
     * @param option
     * @return
     */
    @RequestMapping("/create")
    public FormerResult createAppoint(BuyCardOptionVo option) {
        if (option.getUserId() == null || option.getAddressId() == null) {
            return CommonUtil.FAIL(new FormerResult(), "缺少参数", null);
        }
        return laundryAppointmentService.createAppoint(option);
    }

    /**
     * 开通关闭自动续费
     *
     * @param option
     * @return
     */
    @RequestMapping("/changeAuto")
    @ResponseBody
    public FormerResult changeAuto(BuyCardOptionVo option) {
        FormerResult result = new FormerResult();
        if (option.getIsAuto() == null || option.getUserId() == null ||
                option.getIsAuto() < 1 || option.getIsAuto() > 2) {
            return CommonUtil.FAIL(result,"缺少参数!",null);
        }
        return CommonUtil.SUCCESS(result,"更新成功!",laundryAppointmentService.changeAuto(option));
    }
    /**
     * 获取预约列表
     *
     * @param option
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public FormerResult getList(BuyCardOptionVo option) {
        FormerResult result = new FormerResult();
        if (option.getUserId() == null) {
            return CommonUtil.FAIL(result,"缺少参数",null);
        }
        if (option.getPageSize() == null) {
            option.setPageSize(10);
        }
        if (option.getCurrentPage() == null) {
            option.setCurrentPage(1);
        }
        option.setCurrentPage((option.getCurrentPage() - 1) * option.getPageSize());
        return laundryAppointmentService.getList(option);
    }
}