package com.lrj.VO;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author Lxh
 * @Date 2020/9/8 19:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MiNiUserInfo {
    private String avatarUrl;
    private String city;
    private String country;
    private Integer gender;
    private String language;
    private String nickname;
    private String province;
    
}
