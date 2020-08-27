package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author Lxh
 * @Date 2020/8/27 14:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppleJsonVo {
    private String iss;
    private String aud;
    private String exp;
    private String sub;
    private String nonce;
    private String c_hash;
    private String email;
    private String email_verified;
    private String auth_time;
    private String nonce_supported;
}
