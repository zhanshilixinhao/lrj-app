package com.lrj.service;

import com.lrj.VO.FormerResult;

import javax.servlet.http.HttpServletRequest;

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
}
