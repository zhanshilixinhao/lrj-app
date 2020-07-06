package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lxh
 * @date 2020/4/1 16:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FormerResult {
    private String requestStatus = "";
    private Integer errorCode;
    public static final int SUCCESS_CODE = 200;
    public static final int Fail_CODE = 500;
    private String errorTip = "";
    private Object data;
}
