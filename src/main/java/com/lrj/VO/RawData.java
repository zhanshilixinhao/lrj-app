package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author： Lxh
 * @Date： 2020/6/9 11:13
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class RawData {
    private UserInfoBean userInfo;
    private String encryptedData;
    private String iv;
    private String openid;

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoBean {
        private String nickName;
        private String avatarUrl;
    }
}


