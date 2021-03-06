package com.lrj.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Lxh
 * @date 2020/4/7 14:11
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
@TableName("app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appUserId;
    private String userPhone;
    private String userPassword;
    private String nickName;
    private String headPhoto;
    private String deviceToken;
    private Integer active;
    private String createTime;
    private Integer isCheck;
    private Date checkTime;
    private String invitationCode;
    private Integer isYearsService;
    private Integer yearsServiceType;
    private Integer residueCount;
    private Date yearsServiceStartTime;
    private Date yearsServiceEndTime;
    @JSONField(name = "openid")
    private String openId;
    private String unionId;
    private String verificationCode;
    private Byte age;
    private Integer superId;
    private String email;
    /**
     * 1 APP用户 2. 商家
     */
    private Integer superType;
    public static final String COLUMN_USER_PHONE = "userPhone";
    public static final String APP_USER_ID_COLUMN = "app_user_id";
}
