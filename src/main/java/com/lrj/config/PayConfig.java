package com.lrj.config;

/**
 * @author : cwj
 * @describe : 商户在微信支付平台的参数
 * @date : 2020-4-10
 */
public class PayConfig {
    // 这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
    // 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
    // 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改
    /*****************************小程序提交参数***********************************/
    private static String min_key = "2rSy2j3XMsCebEDa9hUZKuPi1UWbxm5R";
    // 微信分配的公众号ID（开通公众号之后可以获取到）
    private static String min_appID = "wxa560d266e8d31842";
    // 微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
    private static String min_mchID = "1325631701";
    // 接口密钥
    private static String min_APPSECRET = "10147ef8bb9fbc9e344b75c857f0a2e9";
    private static String min_APPSECRET_2 = "5f58b4f94f94f437e982b486a265ea58";
    // 受理模式下给子商户分配的子商户号
    private static String min_subMchID = "";
    // HTTPS证书的本地路径
    private static String min_certLocalPath = "";
    // HTTPS证书密码，默认密码等于商户号MCHID
    private static String min_certPassword = "1325631701";
    // 是否使用异步线程的方式来上报API测速，默认为异步模式
    private static boolean min_useThreadToDoReport = true;
    // 机器IP
    private static String min_ip = "";
    // noncestr
    private static String min_noncestr = "123456789";

    /******************APP支付参数*******************************************/
    private static String app_key = "axuyPyL2tEKEnvArzAvhpA9BuridrGjc";
    //微信分配的公众号ID（开通公众号之后可以获取到）
    private static String app_appID = "wxcbdfe89e9cef31d9";
    //微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
    private static String app_mchID = "1300635601";
    //受理模式下给子商户分配的子商户号
    private static String app_subMchID = "";
    //HTTPS证书的本地路径
    private static String app_certLocalPath = "";
    //HTTPS证书密码，默认密码等于商户号MCHID
    private static String app_certPassword = "";
    //是否使用异步线程的方式来上报API测速，默认为异步模式
    private static boolean app_useThreadToDoReport = true;
    //机器IP
    private static String app_ip = "";

    //以下是几个API的公共路径：
    /** 统一下单接口 **/
    public static String UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /** 获取成员信息 **/
    public static String GETUSERINFOR = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo";
    /** 获取网页授权token **/
    public static String GETTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    /** access_token  API **/
    public static String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
    // 1）被扫支付API
    public static String PAY_API = "https://api.mch.weixin.qq.com/pay/micropay";
    // 2）被扫支付查询API
    public static String PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";
    // 3）退款API
    public static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    // 4）退款查询API
    public static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";
    // 5）撤销API
    public static String REVERSE_API = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
    // 6）下载对账单API
    public static String DOWNLOAD_BILL_API = "https://api.mch.weixin.qq.com/pay/downloadbill";
    // 7) 统计上报API
    public static String REPORT_API = "https://api.mch.weixin.qq.com/payitil/report";
    // 8) JS-SDK签名ticket_API
    public static String TICKET_API = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

    public static String getMin_key() {
        return min_key;
    }

    public static void setMin_key(String min_key) {
        PayConfig.min_key = min_key;
    }

    public static String getMin_appID() {
        return min_appID;
    }

    public static void setMin_appID(String min_appID) {
        PayConfig.min_appID = min_appID;
    }

    public static String getMin_mchID() {
        return min_mchID;
    }

    public static void setMin_mchID(String min_mchID) {
        PayConfig.min_mchID = min_mchID;
    }

    public static String getMin_APPSECRET() {
        return min_APPSECRET;
    }

    public static void setMin_APPSECRET(String min_APPSECRET) {
        PayConfig.min_APPSECRET = min_APPSECRET;
    }

    public static String getMin_APPSECRET_2() {
        return min_APPSECRET_2;
    }

    public static void setMin_APPSECRET_2(String min_APPSECRET_2) {
        PayConfig.min_APPSECRET_2 = min_APPSECRET_2;
    }

    public static String getMin_subMchID() {
        return min_subMchID;
    }

    public static void setMin_subMchID(String min_subMchID) {
        PayConfig.min_subMchID = min_subMchID;
    }

    public static String getMin_certLocalPath() {
        return min_certLocalPath;
    }

    public static void setMin_certLocalPath(String min_certLocalPath) {
        PayConfig.min_certLocalPath = min_certLocalPath;
    }

    public static String getMin_certPassword() {
        return min_certPassword;
    }

    public static void setMin_certPassword(String min_certPassword) {
        PayConfig.min_certPassword = min_certPassword;
    }

    public static boolean isMin_useThreadToDoReport() {
        return min_useThreadToDoReport;
    }

    public static void setMin_useThreadToDoReport(boolean min_useThreadToDoReport) {
        PayConfig.min_useThreadToDoReport = min_useThreadToDoReport;
    }

    public static String getMin_ip() {
        return min_ip;
    }

    public static void setMin_ip(String min_ip) {
        PayConfig.min_ip = min_ip;
    }

    public static String getMin_noncestr() {
        return min_noncestr;
    }

    public static void setMin_noncestr(String min_noncestr) {
        PayConfig.min_noncestr = min_noncestr;
    }

    public static String getApp_key() {
        return app_key;
    }

    public static void setApp_key(String app_key) {
        PayConfig.app_key = app_key;
    }

    public static String getApp_appID() {
        return app_appID;
    }

    public static void setApp_appID(String app_appID) {
        PayConfig.app_appID = app_appID;
    }

    public static String getApp_mchID() {
        return app_mchID;
    }

    public static void setApp_mchID(String app_mchID) {
        PayConfig.app_mchID = app_mchID;
    }

    public static String getApp_subMchID() {
        return app_subMchID;
    }

    public static void setApp_subMchID(String app_subMchID) {
        PayConfig.app_subMchID = app_subMchID;
    }

    public static String getApp_certLocalPath() {
        return app_certLocalPath;
    }

    public static void setApp_certLocalPath(String app_certLocalPath) {
        PayConfig.app_certLocalPath = app_certLocalPath;
    }

    public static String getApp_certPassword() {
        return app_certPassword;
    }

    public static void setApp_certPassword(String app_certPassword) {
        PayConfig.app_certPassword = app_certPassword;
    }

    public static boolean isApp_useThreadToDoReport() {
        return app_useThreadToDoReport;
    }

    public static void setApp_useThreadToDoReport(boolean app_useThreadToDoReport) {
        PayConfig.app_useThreadToDoReport = app_useThreadToDoReport;
    }

    public static String getApp_ip() {
        return app_ip;
    }

    public static void setApp_ip(String app_ip) {
        PayConfig.app_ip = app_ip;
    }
}
