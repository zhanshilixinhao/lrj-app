package com.lrj.service;

import com.github.pagehelper.PageInfo;
import com.lrj.VO.OrderVo;
import com.lrj.dto.RequestDTO;
import com.lrj.dto.ReturnData;
import com.lrj.pojo.Rebate;

/**
 * @author : fl
 * @describe :用户反馈
 * @date : 2020-5-11
 */
public interface RebateService {

    /**
     * 向上级用户返利
     * @param orderVo
     * @return
     */
    void rebate(OrderVo orderVo) ;

}
