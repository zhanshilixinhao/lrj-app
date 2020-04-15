package com.lrj.VO;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lxh
 * @date 2020/4/14 17:40
 */
@Data
@Accessors
@NoArgsConstructor
@AllArgsConstructor
public class WeChatUserInfo {
    @JSONField(name = "openid")
    private String openid;
    @JSONField(name = "nickname")
    private String nickname;
    @JSONField(name = "sex")
    private Integer sex;
    @JSONField(name = "province")
    private String province;
    @JSONField(name = "city")
    private String city;
    @JSONField(name = "country")
    private String country;
    @JSONField(name = "headimgurl")
    private String headimgurl;
    @JSONField(name = "privilege")
    private String privilege;
    @JSONField(name = "unionid")
    private String unionid;
}
