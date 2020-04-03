package com.lrj.common;


import com.lrj.util.DateUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 
 * <b>项目名称</b>：lanrenxiyi<br>
 * <b>类名称</b>：Constant<br>
 * <b>类描述</b>：该类为一些静态常量,如 1,0分别代表启用禁用等一系列常用不可修改的常量.<br>
 * <b>创建人</b>：SAM QZL<br>
 * <b>创建时间</b>：2015-11-3 上午10:20:24<br>
 * <b>修改人</b>：SAM QZL<br>
 * <b>修改时间</b>：2015-11-3 上午10:20:24<br>
 * <b>修改备注</b>：<br>
 * @author SAM QZL<br>
 * @version
 * 
 */
public class Constant {

    /**
     * <!>注意<!><!>注意<!><!>注意<!>
     * <!>注意<!><!>注意<!><!>注意<!>
     * <!>注意<!><!>注意<!><!>注意<!>
     * 数字类型如整型,不能使用Integer来定义,推荐int
     * 因为java会自动拆箱,如果用Integer对象类型定义静态常量,你所比较的也是个Integer类型用
     * ==比较就会不相等,因为是两个内存地址不同的对象。
     * <!>注意<!><!>注意<!><!>注意<!>
     * <!>注意<!><!>注意<!><!>注意<!>
     * <!>注意<!><!>注意<!><!>注意<!>
     * 
     */
    /** 已注册 **/
    public static final int HASREGESITER = 1;
    /** 未注册 **/
    public static final int UNREGESITER = 2;
    /** 优惠券邀请码赠送的ID **/
    public static int COUPONID = 2;
    /** 支付订单后的分享红包 **/
    public static List<Integer> PAYORDERCOUPONID = new ArrayList<Integer>() {

        private static final long serialVersionUID = -5900646957409713128L;
        {
            add(1);
            add(2);
            add(3);
            add(4);
            add(5);
            add(6);
            add(7);
            add(8);
            add(9);
            add(10);
        }
    };;
    /** 被邀请着赠送余额 **/
    public static BigDecimal INVITECODEBANLANCE = new BigDecimal("10.00");
    /** 优惠券系统赠送 **/
    public static final int COUPONSYSTEMSEND = 5;
    /** 优惠券订单使用记录 **/
    public static final int COUPONUSE = 4;
    /** 分享邀请码获取 **/
    public static final int COUPONSHAREINVITECODEGET = 1;
    /** 获取朋友分享 **/
    public static final int COUPONGETFRIENDSHARE = 2;
    /** banner活动获取 **/
    public static final int COUPONGETFROMBANNER = 3;
    /** 订单未完成 **/
    public static final int UNFINISHED = 1;
    /** 订单已完成 **/
    public static final int FINISHED = 2;
    /** 优惠券最低金额比例 **/
    public static final double MINMUCOUNPBILI = 0.25;
    /** 过期 1 **/
    public static final int EXPIRED = 1;
    /** 没过期 0 **/
    public static final int NOEXPIRED = 0;
    /** 1订单退款标记 **/
    public static final int REFUND = 1;
    /** 0此订单非退款 **/
    public static final int NOREFUND = 0;
    /**
     * 1:代表该用户或者角色等为可用状态.
     */
    public static final int ACTIVE = 1;
    /**
     * 0:代表该用户或者角色等为不可用可用状态.
     */
    public static final int FORBIDDEN = 0;
    /**
     * 0:代表请求执行后返回客户端状态为执行成功.
     */
    public static final int YES = 0;
    /**
     * 1:代表请求执行后返回客户端状态为执行失败.
     */
    public static final int NO = 1;
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    /** 普通发票 **/
    public static final int PLAININVOICE = 1;
    /** 增值税发票 **/
    public static final int VALUEADDEDTAXINVOICE = 2;
    /** 逻辑删除 **/
    public static final int DELETE = 1;
    /** 非删除正常状态 **/
    public static final int NODELETE = 0;
    /** 购物车项目加一 **/
    public static final int CARTADD = 0;
    /** 购物车项目减一 **/
    public static final int CARTCUT = 1;
    /** 申请未处理 **/
    public static final int NOHANDLE = 0;
    /** 分享邀请码获取 **/
    public static final int SHAREINVITECODE = 1;
    /** 购物获取 **/
    public static final int SHOPPINGGETPOINT = 2;
    /** 购物消费 **/
    public static final int SHOPPINGCONSUMPTION = 3;
    /** 签到获取 **/
    public static final int SIGNGETPOINT = 4;
    /**
     * 1:代表商品为显示状态.
     */
    public static final int SHOW = 1;
    /**
     * 0:代表商品为显示隐藏状态.
     */
    public static final int HIDDEN = 0;
    /** 商品展示图片 **/
    public static final int ITEMBANNERIMG = 0;
    /** 商品介绍图 **/
    public static final int ITEMINTROIMG = 1;
    /** 商品介绍视频 **/
    public static final int ITEMINTROVIDEO = 2;
    /** 地址 发票默认 **/
    public static final int DEFAULT = 1;
    /** 非默认 **/
    public static final int NODEFAULT = 0;
    /** 订单默认 **/
    public static final int DEFAULTSTATUS = 0;
    /** 订单已支付待发货 **/
    public static final int PAYWAIITTODELIVER = 1;
    /** 已发货待收货物 **/
    public static final int DELIVERWAITTORECEIVE = 2;
    /** 确认收货订单完成 **/
    public static final int CONFIRMRECEIVE = 3;
    /** 订单未支付 **/
    public static final int NONPAYMENT = 1;
    /** 订单已支付 **/
    public static final int PAYMENT = 2;
    /** 订单支付失败 **/
    public static final int PAYMENTFAILED = 3;
    /** 订单未抢 **/
    public static final int UNLOCK = 1;
    /** 订单已抢 **/
    public static final int LOCK = 2;
    /** 线上支付 **/
    public static final int ONLINEPAY = 1;
    /** 线下支付 **/
    public static final int OFFLINEPAY = 2;
    /**
     * 上传图片类型
     */
    /** 商品图片 **/
    public static final int ITEMIMG = 0;
    /** 头像 **/
    public static final int HEADPHOTO = 1;
    /** 轮播图片 **/
    public static final int BANNERIMG = 2;
    /** APP客户端用户 **/
    public static final int APPUSER = 1;
    /** APP企业端用户 **/
    public static final int STAFFUSER = 2;
    /** 后台管理用户 **/
    public static final int ADMIN = 3;
    /** 未读 **/
    public static final int NOREAD = 1;
    /** 已读 **/
    public static final int READ = 2;
    /** 关于我们 **/
    public static final int ABOUTUS = 1;
    /** 帮助信息 **/
    public static final int HELP = 2;
    /** 法律声明 **/
    public static final int LAW = 3;
    /** 充值 **/
    public static final int CHARGE = 1;
    /** 消费 **/
    public static final int EXPAND = 2;
    /** 过期 **/
    public static final int EXPIREDLOG = 3;
    /** 消费超80送的 **/
    public static final int EXPANDTHAN80 = 4;
    /** 首单赠送 **/
    public static final int FIRSTORDERSEND = 5;
    /** 系统修改 **/
    public static final int SYSTEMSET = 6;
    /** 扫码赠送 **/
    public static final int SACNSEND = 7;
    /** 输入邀请码赠送 **/
    public static final int INVITECODEGET = 8;
    /** 收益兑换 **/
    public static final int EARNINGSEXCHANGEGET = 9;
    /** 问卷获取 **/
    public static final int QUESTION_NAIRE_SURVEY = 10;
    /** window 客户端签名密钥 **/
    public static final String WINDOWSECRETKEY = "6d5lJc1hqYCc78RA6gNU3ZC2A2wYuSPv";
    /** Android密钥 **/
    public static final String ANDROIDSECRETKEY = "7d5lJc1hqYCc78RA6gNU3ZC2A2wYuSPv";
    /** IOS密钥 **/
    public static final String IOSSECRETKEY = "8d5lJc1hqYCc78RA6gNU3ZC2A2wYuSPv";
    /** 会员订单 **/
    public static final int YEARSSERVICE = 1;
    /** 空结果 **/
    private static Map<String, Object> MAP;
    /** APP分销类型 **/
    public static final int APPDISTYPE = 1;
    /** 商家后台分销类型 **/
    public static final int BUSSDISTYPE = 2;
    /** ---------------收益记录--------------------- **/
    /** 推广收益 **/
    public static final int GENERALIZEEARNINGS = 1;
    /** 兑换余额 **/
    public static final int EXCHANGEBALANCE = 2;
    /** 提现 **/
    public static final int WITHDRAWEARNING = 3;
    /** 提现退款 **/
    public static final int WITHDRAWREFUND = 4;
    /** ----------------提现状态-------------------- **/
    /** 申请提交 **/
    public static final int WITHDRAWAPPLYSTATE = 1;
    /** 提现成功 **/
    public static final int WITHDRAWSUCCESS = 2;
    /** 提现失败 **/
    public static final int WITHDRAWFAIL = 3;
    /** -----------------收益比例------------------- **/
    /** app用户分销收益比 暂定0.09 **/
    public static BigDecimal APPEARNINGSRATIO = new BigDecimal("0.09");
    /** 后台商家用户分销收益比 暂定0.06 **/
    public static BigDecimal BUSSEARNINGSRATIO = new BigDecimal("0.06");
    /** -----------------收益比例------------------- **/
    /** -----------------会员日及活动参数------------------- **/
    /** 会员日暂定每月17日 **/
    public static int MEMBER_DAY = 17;
    /** 会员折扣暂定7折扣 **/
    public static BigDecimal MEMBER_DISCOUNT = new BigDecimal("0.68");
    /** 打折活动开始日期 **/
    public static Date ACTIVITY_BEGIN = DateUtils.formatStringToDate("2017-03-01", "yyyy-MM-dd");
    /** 打折活动结束日期 **/
    public static Date ACTIVITY_END = DateUtils.formatStringToDate("2017-03-26", "yyyy-MM-dd");
    /** 打折活动折扣比例 **/
    public static BigDecimal ACTIVITY_DISCOUNT = new BigDecimal("0.80");

    /** -----------------会员日及参数------------------- **/
    /** 单例MAP对象 **/
    /** 空对象 **/
    public synchronized static Map<String, Object> NULL() {

        if (MAP != null) {
            return MAP;
        }
        else {
            MAP = new HashMap<String, Object>();
            return MAP;
        }
    }
}
