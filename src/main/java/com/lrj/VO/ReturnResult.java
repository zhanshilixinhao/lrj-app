package com.lrj.VO;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lxh
 * @date 2020/4/14 15:34
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReturnResult {
    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    @JSONField(name = "openid")
    private String openId;
    private String scope;
    @JSONField(name = "errcode")
    private String errCode;
    @JSONField(name = "errmsg")
    private String errMsg;
    /**会话密钥*/
    @JSONField(name = "session_key")
    private String sessionKey;
    @JSONField(name = "unionid")
    private String unionId;
}
