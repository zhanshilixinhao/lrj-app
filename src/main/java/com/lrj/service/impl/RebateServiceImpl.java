package com.lrj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lrj.VO.OrderVo;
import com.lrj.VO.StaffInfoVo;
import com.lrj.VO.UserInfoVo;
import com.lrj.VO.UserLevelVo;
import com.lrj.constant.Constant;
import com.lrj.dto.RequestDTO;
import com.lrj.dto.ReturnData;
import com.lrj.mapper.RebateMapper;
import com.lrj.pojo.Merchant;
import com.lrj.pojo.Rebate;
import com.lrj.pojo.User;
import com.lrj.pojo.UserLevel;
import com.lrj.service.IMerchantService;
import com.lrj.service.IStaffService;
import com.lrj.service.IUserService;
import com.lrj.service.RebateService;
import com.lrj.util.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private IUserService userService;
    @Resource
    private RebateMapper rebateMapper;
    @Resource
    private IMerchantService merchantService;
    @Override
    public void rebate(OrderVo orderVo) {
        UserInfoVo userInfoVo = userService.findUserInfoByUserId(orderVo.getUserId());
        Rebate rebate = new Rebate();
        if(userInfoVo.getSuperId()!=null){
            switch (userInfoVo.getSuperType()){
                case 1:  //APP用户
                    UserLevelVo userLevelVo = userService.findUserLevelInfo(userInfoVo.getSuperId());
                    BigDecimal rebateMoney1 = new BigDecimal(orderVo.getTotalPrice().doubleValue()*userLevelVo.getDistributionRatio().doubleValue());
                    rebate.setBackMoney(rebateMoney1);
                    rebate.setUserId(userInfoVo.getSuperId());
                    break;
                case 2:  //引流商家
                    Merchant merchant = merchantService.findMerchantInfoById(userInfoVo.getSuperId());
                    BigDecimal rebateMoney2 = new BigDecimal(orderVo.getTotalPrice().doubleValue()*merchant.getDistributionRatio().doubleValue());
                    rebate.setBackMoney(rebateMoney2);
                    rebate.setUserId(merchant.getMerchantId());
                    break;
            }
            rebate.setCreateTime(DateUtils.getNowtime());
            rebate.setType(userInfoVo.getSuperType());
            rebate.setLowId(userInfoVo.getAppUserId());
            rebate.setSource("消费返利");
            rebate.setOrderNumber(orderVo.getOrderNumber());
            rebateMapper.add(rebate);
        }
    }
}
