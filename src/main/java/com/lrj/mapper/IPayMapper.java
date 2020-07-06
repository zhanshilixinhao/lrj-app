package com.lrj.mapper;

import com.lrj.VO.OrderVo;
import com.lrj.VO.Order_custom_houseServiceVo;
import com.lrj.VO.Order_houseServiceVo;
import com.lrj.VO.Order_washingVo;
import com.lrj.pojo.BalanceRecord;
import com.lrj.pojo.PayOperation;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author : cwj
 * @describe : 支付 有关接口
 * @date : 2020-4-27
 */
@Repository
public interface IPayMapper {

    /**
     * 用户微信支付 流水记录
     * @param flowRecordMap
     */
    void WXPayFlowRecord(Map<String, Object> flowRecordMap);

    /**
     * 支付流水
     * @param payOperation
     */
    void payFlowRecord(PayOperation payOperation);

    /**
     * 提现申请
     * @param payOperation
     * @return
     */
    Integer userWithdrawApply(PayOperation payOperation);

    /**
     * 增加用户余额 变动记录
     * @param balanceRecord
     */
    void addUserBalanceRecord(BalanceRecord balanceRecord);

    /**
     * 更新用户 余额记录 状态
     * @param rechargeOrderNumber
     */
    void updateUserBalanceRecord(String rechargeOrderNumber);
}
