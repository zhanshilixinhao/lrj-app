package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 商家管理类
 * @Author Lxh
 * @Date 2020/6/29 15:58
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "merchant")
public class Merchant{
    private static final long serialVersionUID = 2607786623630077533L;
    @Id
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
    private Date createTime;
    private Date updateTime;
    /**二维码*/
    private String qrCode;
    /**二维码 剩余可销售次数*/
    private Integer saleCount;//剩余可销售次数
     private String saleEndTime;//销售结束时间
    /**商家可用于销售的订单Id*/
    private Integer merchantOrderId;//

    public static final String COLUMN_MERCHANT_ACTIVE = "active";
    public static final int ACTIVE_TRUE = 1;
    public static final String COLUMN_MERCHANT_NAME = "name";
    public static final String COLUMN_MERCHANT_PHONE = "phone";
    public static final String COLUMN_MERCHANT_TYPE = "type";
}
