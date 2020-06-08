package com.lrj.service.impl;


import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;



/**
 * @author Lxh
 * @date 2020/5/6 17:50
 */
@Service
public class AliPayLogInServiceImpl implements InitializingBean {

    /**Alipay客户端*/
    private AlipayClient alipayClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        //alipayClient = new DefaultAlipayClient();
    }
}
