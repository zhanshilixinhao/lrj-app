package com.lrj.service.impl;

import com.lrj.VO.BannerVo;
import com.lrj.mapper.IBannerMapper;
import com.lrj.mapper.IMerchantMapper;
import com.lrj.pojo.Merchant;
import com.lrj.service.IBannerService;
import com.lrj.service.IMerchantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 商家信息服务实现层
 * @date : 2020-7-15
 */
@Service
@Transactional
public class MerchantServiceImpl implements IMerchantService{
    @Resource
    private IMerchantMapper merchantMapper;


    @Override
    public Merchant findMerchantInfoById(Integer merchantId) {
        return merchantMapper.getMerchantInfoById(merchantId);
    }

    @Override
    public void updateSaleCount(int saleCount, Integer merchantId) {
        //用Map封装多个参数
        Map<String, Integer> params = new HashMap<>();
        params.put("saleCount", saleCount);
        params.put("merchantId", merchantId);
        merchantMapper.updateSaleCount(params);
    }
}
