package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Lxh
 * @date 2020/4/7 11:19
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Component
@PropertySource("classpath:/properties/messages.properties")
public class LoginMess {
    @Value("${login.prikey}")
    private String prikey;//登录秘钥
    private Long Id; //流水号，请求出错时可能为空
    private String exID; //开发者自定义的id，若请求时为空返回为空
    private Integer code; //返回码
    private String content; //返回码说明
    private String phone; //加密后的手机号码
}
