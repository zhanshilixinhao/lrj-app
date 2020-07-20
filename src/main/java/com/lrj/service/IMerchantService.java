package com.lrj.service;

import com.lrj.pojo.Merchant;

/**
 * @author : cwj
 * @describe : 商家信息服务层
 * @date : 2020-7-15
 */

public interface IMerchantService {
    /**
     * 通过ID 查询商家信息
     */
    Merchant findMerchantInfoById(Integer merchantId);

    /**
     * 更新销售商家 销售次数
     * @param i
     * @param merchantId
     */
    void updateSaleCount(int i, Integer merchantId);
}
