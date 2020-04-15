package com.lrj.VO;

/**
 * 回调数据对象
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 被扫支付提交Post数据给到API之后，API会返回XML格式的数据，这个类用来装这些数据
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class NotifyData {

    // 协议层
    private String return_code = "";
    private String return_msg = "";
    // 协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
    private String appid = "";
    private String mch_id = "";
    private String nonce_str = "";
    private String sign = "";
    private String result_code = "";
    private String err_code = "";
    private String err_code_des = "";
    private String device_info = "";
    // 业务返回的具体数据（以下字段在return_code 和result_code 都为SUCCESS 的时候有返回）
    private String openid = "";
    private String is_subscribe = "";
    private String trade_type = "";
    private String bank_type = "";
    private String total_fee = "";
    private String coupon_fee = "";
    private String fee_type = "";
    private String transaction_id = "";
    private String out_trade_no = "";
    private String attach = "";
    private String time_end = "";

}
