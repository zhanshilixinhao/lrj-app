package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

<<<<<<< HEAD
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
=======
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
>>>>>>> origin/master

/**
 * @Description: 商家管理类
 * @Author Lxh
 * @Date 2020/6/29 15:58
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
<<<<<<< HEAD
@Table(name = "merchant")
public class Merchant{
    private static final long serialVersionUID = 2607786623630077533L;
    @Id
=======

@Table(name = "merchant")
public class Merchant extends Base {
    private static final long serialVersionUID = 2607786623630077533L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
>>>>>>> origin/master
    private Integer merchantId;
    private String name;
    private Byte type;
    private String address;
    private String phone;
    /**商家联系人*/
    private String principal;
    private String merchantInfo;
    /**分销比例*/
    private BigDecimal distributionRatio;
    /**提成比例*/
    private BigDecimal commissionRatio;
    private Byte active;
<<<<<<< HEAD
    private Date createTime;
    private Date updateTime;
    /**二维码*/
    private String qrCode;
    /**二维码 剩余可销售次数*/
    private Integer saleCount;//剩余可销售次数
     private String saleEndTime;//销售结束时间
    /**商家可用于销售的订单Id*/
    private Integer merchantOrderId;//
=======
    /**二维码*/
    private String qrCode;
>>>>>>> origin/master

    public static final String COLUMN_MERCHANT_ACTIVE = "active";
    public static final int ACTIVE_TRUE = 1;
    public static final String COLUMN_MERCHANT_NAME = "name";
    public static final String COLUMN_MERCHANT_PHONE = "phone";
    public static final String COLUMN_MERCHANT_TYPE = "type";
}
