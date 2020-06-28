package com.lrj.service;

import com.lrj.VO.FormerResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:
 * @Author: Lxh
 * @Date: 2020/6/23 16:02
 */

public interface AppletsPayService {

    /**
     * @Description: 微信第三方支付
     * @Author: LxH
     * @Date: 2020/6/23 16:05
     */
    FormerResult pay(HttpServletRequest request);

    /**
     * @Description: 微信第三方支付回调
     * @Author: LxH
     * @Date: 2020/6/24 15:55
     */
    void appletsPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
