package com.lrj.mapper;

import com.lrj.pojo.Merchant;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author : cwj
 * @describe : 商家信息 持久层
 * @date : 2020-7-15
 */
@Repository
public interface IMerchantMapper {
    /**
     * 通过Id查询商家信息
     */
    Merchant getMerchantInfoById(Integer merchantId);

    /**
     * 更新销售商家 销售次数
     * @param params
     */
    void updateSaleCount(Map<String,Integer> params);
}
