package com.lrj.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
public class User {
    @Id
    private Integer appUserId;
    private String userPhone;
    private String userPassword;
    private String nickName;
    private String headPhoto;
    private String deviceToken;
    private Integer active;
    private Date createTime;
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
}
