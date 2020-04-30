package com.lrj.VO;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.print.attribute.standard.MediaSize;
import java.io.Serializable;

/**
 * @author Lxh
 * @date 2020/4/29 16:09
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WxUserInfo implements Serializable {
    private static final long serialVersionUID = -3968597117111931008L;
    @JSONField(name = "openid")
    private String openId;
    @JSONField(name = "nickname")
    private String nickName;
    @JSONField(name = "sex")
    private Integer sex;
    @JSONField(name = "province")
    private String province;
    @JSONField(name = "city")
    private String city;
    @JSONField(name = "privilege")
    private String[] privilege;
    @JSONField(name = "headimgurl")
    private String headImgUrl;
    @JSONField(name = "unionid")
    private String unionId;
    @JSONField(name = "country")
    private String country;
}
