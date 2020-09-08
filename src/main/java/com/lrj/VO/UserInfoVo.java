package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : cwj
 * @describe : app 用户实体类
 * @date : 2020-4-7
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo {
        private Integer appUserId; //
        private String userPhone = ""; //
        private String userPassword = ""; //
        private String nickname = ""; //
        private String headPhoto = ""; //
        private String deviceToken = ""; //
        private Integer active; //
        private String createTime = ""; //
        private Integer ischeck;
        private String inviteCode; //邀请码
        private String invitedCode; //上级邀请码
        private Integer superId;  //上级ID
        private String checkTime = "";
        private Integer isYearsService;
        private String aliAccount; //支付宝账号
        private String realityName;//真实名字
        private Integer type; //用户类型
        private Integer superType;//上级用户类型
}
