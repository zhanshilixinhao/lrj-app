package com.lrj.service;

import com.lrj.VO.AliPayVo;

import java.io.IOException;
import java.util.Map;

public interface IPayService {

    /**
     * 微信支付回调
     *
     * @param xml 微信支付参数
     * @return
     */
    String wxminiPayNotify(String xml) throws Exception;


    /**
     * app月卡购买支付宝回调
     * @param aliPayVo
     * @param parameterMap
     * @return
     */
    String appAliPayNotify(AliPayVo aliPayVo, Map parameterMap) throws Exception;


    /**
     * app月卡购买微信支付回调
     * @param xml
     * @return
     * @throws IOException
     */
    String appWXPayNotify(String xml) throws Exception;
}
