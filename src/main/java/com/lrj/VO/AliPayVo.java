package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AliPayVo {
	private String notify_time; //通知时间
	private String notify_type; //通知类型
	private String notify_id;   //通知校验ID
	private String sign_type;	//签名方式
	private String sign;		//签名
	private String out_trade_no;//商户网站唯一订单号
	private String subject;		//商品名称
	private String payment_type;//支付类型
	private String trade_no;	//trade_no
	private String trade_status;//交易状态
	private String seller_id;	//卖家支付宝用户号
	private String seller_email;//卖家支付宝账号
	private String buyer_id;	//买家支付宝用户号
	private String buyer_email;	//买家支付宝账号
	private String total_fee;	//交易金额
	private String quantity;	//购买数量
	private String price;		//商品单价
    private String body;		//商品描述
	private String gmt_create;	//交易创建时间
	private String gmt_payment;	//交易付款时间
	private String is_total_fee_adjust;//是否调整总价
	private String use_coupon;	//是否使用红包买家
	private String discount;	//折扣
	private String refund_status;//退款状态
	private String gmt_refund;	//退款时间

}
