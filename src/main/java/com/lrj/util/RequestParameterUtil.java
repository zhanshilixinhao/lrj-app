package com.lrj.util;

import com.lrj.VO.FormerResult;
import com.lrj.VO.Result;
import com.lrj.common.Constant;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lxh
 * @date 2020/4/1 16:14
 */
public class RequestParameterUtil {

    public static Map<String, Object> transferRequestParameters(HttpServletRequest request, String[] tarAttr, String[] types, FormerResult result) {

        if (request == null || tarAttr == null || types == null || result == null) {
            throw new RuntimeException("all paramters can not be null!");
        }
        if (tarAttr.length != types.length) {
            throw new RuntimeException("tarAttr types size must be same!");
        }
        Map<String, Object> parmaters = new HashMap<String, Object>();
        for (int i = 0; i < tarAttr.length; i++) {
            String temp = request.getParameter(tarAttr[i]);
            /** 如果该字段为空就不加入MAP **/
            if (temp == null || "".equals(temp.trim())) {
                continue;
            }
            String type = types[i];
            if ("Integer".equals(type) || "int".equals(type)) {
                parmaters.put(tarAttr[i], Integer.parseInt(temp));
            }
            else if ("Long".equals(type) || "long".equals(type)) {
                parmaters.put(tarAttr[i], Long.parseLong(temp));
            }
            else if ("String".equals(type)) {
                parmaters.put(tarAttr[i], temp);
            }
            else if ("BigDecimal".equals(type)) {
                parmaters.put(tarAttr[i], new BigDecimal(temp));
            }
            else {
                parmaters.put(tarAttr[i], temp);
            }
        }
        return (parmaters);
    }

    public static boolean checkRequestParametersIsNull(HttpServletRequest request, String[] tarAttr, FormerResult result) {

        if (request == null || tarAttr == null || tarAttr.length == 0 || result == null) {
            throw new RuntimeException("request or tarAttr can not be null!");
        }
        for (String s : tarAttr) {
            String value = request.getParameter(s);
            if (value == null || "".equals(value.trim())) {
                result.setRequestStatus(Constant.SUCCESS);
                result.setErrorCode(Constant.NO);
                result.setErrorTip("参数错误,不能为空!");
                return (true);
            }
        }
        return (false);
    }
}
