package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author Lxh
 * @Date 2020/8/26 19:05
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppleVo {
    private String kty;
    private String kid;
    private String use;
    private String alg;
    private String n;
    private String e;
}
