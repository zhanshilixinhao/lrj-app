package com.lrj.VO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Lxh
 * @date 2020/4/30 9:39
 */
@PropertySource("classpath:/properties/messages.properties")
public class Information {
    @Value("${bind.apikey}")
    private String apiKey;
    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.secret}")
    private String secret;
}
