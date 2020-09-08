package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author : cwj
 * @describe : 老项目数据库 数据实体
 * @date : 2020-8-24
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserFromOldDataBase {

    private Integer app_user_id;
    private String user_phone;
    private String user_password;
    private String nickname;
    private String head_photo;
    private String device_token;
    private Integer active;
    private Date create_time;
    private Integer ischeck;
    private Date checktime;
    private String invitation_code;
    private Integer isYearsService;
    private Integer yearsServiceType;
    private Integer residueCount;
    private Date yearsServiceStartTime;
    private Date yearsServiceEndTime;

}
