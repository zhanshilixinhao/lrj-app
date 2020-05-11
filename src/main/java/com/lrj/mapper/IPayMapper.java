package com.lrj.mapper;

import com.lrj.VO.OrderVo;
import com.lrj.VO.Order_custom_houseServiceVo;
import com.lrj.VO.Order_houseServiceVo;
import com.lrj.VO.Order_washingVo;
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
     * @param flowRecordMap
     */
    void payFlowRecord(Map<String, Object> flowRecordMap);
}
