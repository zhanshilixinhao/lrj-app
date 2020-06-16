package com.lrj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lrj.VO.OrderVo;
import com.lrj.VO.StaffInfoVo;
import com.lrj.VO.UserLevelVo;
import com.lrj.constant.Constant;
import com.lrj.dto.RequestDTO;
import com.lrj.dto.ReturnData;
import com.lrj.mapper.RebateMapper;
import com.lrj.pojo.Rebate;
import com.lrj.pojo.User;
import com.lrj.service.IStaffService;
import com.lrj.service.RebateService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.lrj.dto.ReturnData.Fail_CODE;
import static com.lrj.dto.ReturnData.SUCCESS_CODE;
import static com.lrj.pojo.Rebate.USER_ID_COLUMN;

/**
 * @author fl
 * @descrip:
 * @date 2020/6/12 0012下午 2:11
 */
@Service
public class RebateServiceImpl implements RebateService {
    private RebateMapper rebateMapper;
    private ObjectMapper objectMapper;
    private IUserServiceImpl userService;
    private IStaffService staffService;

    public RebateServiceImpl(RebateMapper rebateMapper, ObjectMapper objectMapper, IUserServiceImpl userService, IStaffService staffService) {
        this.rebateMapper = rebateMapper;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.staffService = staffService;
    }

    public PageInfo<Rebate> getPageByParam(RequestDTO requestDTO) {
        QueryWrapper<Rebate> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc(Rebate.CREATE_TIME);
        Rebate rebate = null;
        try {
            rebate = objectMapper.convertValue(requestDTO.getObject(), Rebate.class);
            if (rebate != null && rebate.getUserId() != null) {
                queryWrapper.eq(USER_ID_COLUMN, rebate.getUserId());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        PageHelper.startPage(requestDTO.getPage(),requestDTO.getSize());
        List<Rebate> list = rebateMapper.selectList(queryWrapper);
        return new PageInfo<Rebate>(list);
    }

    public ReturnData<Boolean> add(Rebate rebate) {
        try {
            if (rebateMapper.insert(rebate) > 0) {
                return new ReturnData(SUCCESS_CODE,"操作成功", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ReturnData(Fail_CODE,"操作失败",false );
    }

    @Override
    public ReturnData<Boolean> rebate(OrderVo orderVo) {
        //月卡和定制家政的固定返利比例
        BigDecimal fixRatio = new BigDecimal(0.05);
        BigDecimal rebateAmount = new BigDecimal(0);
        User user = new User();
        user.setAppUserId(orderVo.getUserId());
        User user1 = userService.getAppUserByParam(user);
        if (user1 != null && user1.getSuperId() != null && user1.getSuperType() != null) {
            user.setAppUserId(user1.getSuperId());
            //普通用户
            if (user1.getSuperType() == Constant.APP_USER) {
                User superUser = userService.getAppUserByParam(user);
                if (superUser != null) {
                    UserLevelVo level = userService.findUserLevelInfo(superUser.getAppUserId());
                    if (level != null && level.getDistributionRatio() != null && level.getDistributionRatio().compareTo(BigDecimal.ZERO) > 0) {
                        if (orderVo.getOrderType() == OrderVo.CUSTOM_HOUSE || orderVo.getOrderType() == OrderVo.MONTH_WASHING) {
                            rebateAmount = orderVo.getTotalPrice().multiply(fixRatio);
                        } else {
                            rebateAmount = orderVo.getTotalPrice().multiply(level.getDistributionRatio());
                        }
                        Rebate rebate = new Rebate();
                        rebate.setBackMoney(rebateAmount).setUserId(level.getUserId()).setLowId(orderVo.getUserId())
                                .setCreateTime(LocalDateTime.now()).setType(Constant.APP_USER).setOrderNumber(orderVo.getOrderNumber());
                       return this.add(rebate);
                    }
                }
            } else {
                //商家用户
                StaffInfoVo staffInfoVo = staffService.getStaffInfoByStaffId(user1.getSuperId());
                if (staffInfoVo != null && staffInfoVo.getBusinessDistributionRatio() != null && staffInfoVo.getBusinessDistributionRatio().compareTo(new Double(0)) > 0) {
                    if (orderVo.getOrderType() == OrderVo.CUSTOM_HOUSE || orderVo.getOrderType() == OrderVo.MONTH_WASHING) {
                        rebateAmount = orderVo.getTotalPrice().multiply(fixRatio);
                    } else {
                        rebateAmount = orderVo.getTotalPrice().multiply(BigDecimal.valueOf(staffInfoVo.getBusinessDistributionRatio()));
                    }
                    Rebate rebate = new Rebate();
                    rebate.setBackMoney(rebateAmount).setUserId(staffInfoVo.getSysAdminId()).setLowId(orderVo.getUserId())
                            .setCreateTime(LocalDateTime.now()).setType(Constant.SHOP_USER).setOrderNumber(orderVo.getOrderNumber());
                    return  this.add(rebate);
                }
            }
        }
        return new ReturnData(Fail_CODE,"订单参数错误",false );
    }
}
