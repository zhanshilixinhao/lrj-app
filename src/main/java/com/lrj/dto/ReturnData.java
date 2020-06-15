package com.lrj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author fl
 * @Description: 返回数据结构
 * @date 2020/5/6 0006上午 10:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ReturnData<T>{
    /**
     * 返回状态码
     */
    private Integer code;

    public static final int SUCCESS_CODE = 200;
    public static final int Fail_CODE = 500;
    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回实体
     */
    private T  Object;


}
