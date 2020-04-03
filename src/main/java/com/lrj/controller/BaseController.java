package com.lrj.controller;

import com.lrj.VO.ResultVo;
import com.lrj.constant.Constant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * <b>项目名称</b>：lanrenxiyi<br>
 * <b>类名称</b>：BaseController<br>
 * <b>类描述</b>：controller基类<br>
 * <b>创建人</b>：SAM QZL<br>
 * <b>创建时间</b>：2016-1-7 上午10:21:29<br>
 * <b>修改人</b>：SAM QZL<br>
 * <b>修改时间</b>：2016-1-7 上午10:21:29<br>
 * <b>修改备注</b>：<br>
 * 
 * @author SAM QZL<br>
 * @version
 * 
 */
@SuppressWarnings("deprecation")
public class BaseController {

    /** 共享数据区域 **/
    protected static Map<String, Object> context = new ConcurrentHashMap<String, Object>();



    /**
     * <b>SUCCESS</b>：(三期接口使用的接口成功方法)<br>
     * @return FormerResult<br>
     * @Exception<br>
     * @author SAM QZL
     */
    public ResultVo SUCCESS2(ResultVo result, String tip, Object data) {

        result.setRequestStatus(Constant.SUCCESS);
        result.setErrorCode(Constant.YES);
        result.setErrorTip("获取成功!");
        result.setData(null);
        if (tip != null && !"".equals(tip.trim())) {
            result.setErrorTip(tip);
        }
        return result;
    }

    /**
     * <b>success</b>：(三期接口执行失败!)<br>
     * @return FormerResult<br>
     * @Exception<br>
     * @author SAM QZL
     */
    public ResultVo FAIL2(ResultVo result, String tip, Object data) {

        result.setRequestStatus(Constant.SUCCESS);
        result.setErrorCode(Constant.NO);
        result.setErrorTip("程序异常!");
        result.setData(null);
        if (tip != null && !"".equals(tip.trim())) {
            result.setErrorTip(tip);
        }
        return result;
    }

}
