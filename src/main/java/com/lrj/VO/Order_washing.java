package com.lrj.VO;

/**
 * @author : cwj
 * @describe : 单项洗衣
 * @date : 2020-4-17
 */
public class Order_washing extends OrderVo{
        private Integer takeAddress; // 取件地址
    private Integer receiveAddress; // 送件地址
    private Integer isLock; //  订单是否已抢
    private Integer traceStatus; //  订单当前追踪状态
    private String takeTime = ""; // 取件时间
}
