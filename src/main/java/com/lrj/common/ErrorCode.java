package com.lrj.common;

/**
 * @author linqin
 * @date 2018/9/4
 */
public enum ErrorCode {

    MONTH_CARD_NOT_EXIST(5000, "购买的月卡不存在"),
    NEED_OPTION_TIME(5001, "请选择上门时间!"),
    MONTH_CARD_EWXIST(5002, "您已购买了一张月卡,请勿重复购买!"),
    NO_MONTH_CARD(5003, "无可用月卡"),
    MONTH_CARD_UNUSED(5004, "月卡次数已用完或已过期"),
    ACC_DISABLED(5005, "该账号已被禁用"),
    CARD_DIFF(5006, "只能给当前月卡续费!"),
    NEED_BIND_PHONE(6001, "需要绑定手机号!"),
    OPENID_DISABLED(6002, "操作无效或过期!"),
    VERIFY_CODE_ERR(6003, "短信验证码错误!"),
    BIND_PHONE_EXIST(6004, "该手机号已绑定!"),
    WX_PAY_ERR(6005, "位置支付错误!"),
    ACC_NOT_EXIST(6006, "账号不存在!"),
    OPTION_TIME_ERR(6008, "上门时间错误!"),
    RECORD_NOT_EXIST(6009, "月卡购买订单不存在!!"),
    APPOINT_FORBID(6007, "该月卡不用预约!");

    int code;
    String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
